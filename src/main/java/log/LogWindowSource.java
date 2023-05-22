package log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.collections4.map.LinkedMap;

/**
 * Что починить:
 * 1. Этот класс порождает утечку ресурсов (связанные слушатели оказываются
 * удерживаемыми в памяти)
 * 2. Этот класс хранит активные сообщения лога, но в такой реализации он 
 * их лишь накапливает. Надо же, чтобы количество сообщений в логе было ограничено 
 * величиной m_iQueueLength (т.е. реально нужна очередь сообщений 
 * ограниченного размера) 
 */
public class LogWindowSource
{
    private int m_iQueueLength;
    
    private final LinkedMap<Integer, LogEntry> m_messages;
    private final ArrayList<LogChangeListener> m_listeners;
    private volatile LogChangeListener[] m_activeListeners;
    
    public LogWindowSource(int iQueueLength) 
    {
        m_iQueueLength = iQueueLength;
        m_messages = new LinkedMap(iQueueLength);
        m_listeners = new ArrayList<LogChangeListener>();
    }
    
    public void registerListener(LogChangeListener listener)
    {
        synchronized(m_listeners)
        {
            m_listeners.add(listener);
            m_activeListeners = null;
        }
    }
    
    public void unregisterListener(LogChangeListener listener)
    {
        synchronized(m_listeners)
        {
            m_listeners.remove(listener);
            m_activeListeners = null;
        }
    }
    
    public void append(LogLevel logLevel, String strMessage)
    {
        LogEntry entry = new LogEntry(logLevel, strMessage);
        synchronized (m_messages){
            if (size() < m_iQueueLength)
                m_messages.put(size() + 1, entry);
            else
                m_messages.put(m_messages.lastKey() + 1, entry);
            if (size() > m_iQueueLength)
                m_messages.remove(0);
        }
        LogChangeListener [] activeListeners = m_activeListeners;
        if (activeListeners == null)
        {
            synchronized (m_listeners)
            {
                if (m_activeListeners == null)
                {
                    activeListeners = m_listeners.toArray(new LogChangeListener [0]);
                    m_activeListeners = activeListeners;
                }
            }
        }
        for (LogChangeListener listener : activeListeners)
        {
            listener.onLogChanged();
        }
    }
    
    public int size()
    {
        return m_messages.size();
    }

    public Iterable<LogEntry> range(int startFrom, int count)
    {
        if (startFrom < 0 || startFrom >= m_messages.size())
        {
            return Collections.emptyList();
        }
        int indexTo = Math.min(startFrom + count, m_messages.size());
        List<LogEntry> list = new ArrayList<>();
        for (int i = startFrom; i < indexTo; i++){
            list.add(m_messages.getValue(i));
        }
        return list;
    }

    public Iterable<LogEntry> all()
    {
        return m_messages.values();
    }
}
