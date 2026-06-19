import React, { useState, useRef, useEffect } from 'react';
import { fetchWithAuth } from '../utils/api';
import { Bell, CheckCircle, Info, AlertTriangle, Clock } from 'lucide-react';
import { motion, AnimatePresence } from 'motion/react';

interface Notification {
  id: string;
  title: string;
  message: string;
  type: 'info' | 'success' | 'alert';
  time: string;
  read: boolean;
}

const MOCK_NOTIFICATIONS: Notification[] = [
  {
    id: '1',
    title: 'Posible avistamiento',
    message: 'Alguien cree haber visto a Max cerca de tu última ubicación.',
    type: 'alert',
    time: '5m',
    read: false
  },
  {
    id: '2',
    title: 'Reporte publicado',
    message: 'Tu reporte de Luna ha sido publicado exitosamente.',
    type: 'success',
    time: '2h',
    read: true
  },
  {
    id: '3',
    title: 'Comunidad activa',
    message: 'Hay 5 nuevos voluntarios patrullando tu zona.',
    type: 'info',
    time: '4h',
    read: true
  }
];

export const NotificationMenu = () => {
  const [isOpen, setIsOpen] = useState(false);
  const [notifications, setNotifications] = useState<Notification[]>([]);
  const menuRef = useRef<HTMLDivElement>(null);

  const unreadCount = notifications.filter(n => !n.read).length;

  useEffect(() => {
    const handleClickOutside = (event: MouseEvent) => {
      if (menuRef.current && !menuRef.current.contains(event.target as Node)) {
        setIsOpen(false);
      }
    };
    document.addEventListener('mousedown', handleClickOutside);
    return () => document.removeEventListener('mousedown', handleClickOutside);
  }, []);

  useEffect(() => {
    const fetchNotifications = async () => {
      try {
        const response = await fetchWithAuth('/api/notifications');
        if (response.ok) {
          const data = await response.json();
          setNotifications(data);
        }
      } catch (err) {
        console.error('Error fetching notifications:', err);
      }
    };

    fetchNotifications();
    // Poll every minute
    const interval = setInterval(fetchNotifications, 60000);
    return () => clearInterval(interval);
  }, []);

  const markAllRead = async () => {
    try {
      const response = await fetchWithAuth('/api/notifications/read-all', {
        method: 'PATCH'
      });
      if (response.ok) {
        setNotifications(notifications.map(n => ({ ...n, read: true })));
      }
    } catch (err) {
      console.error('Error marking as read:', err);
    }
  };

  return (
    <div className="relative" ref={menuRef}>
      <button 
        onClick={() => setIsOpen(!isOpen)}
        className={`p-2 rounded-full transition-all relative ${isOpen ? 'bg-primary/10 text-primary' : 'hover:bg-surface-high text-text-muted'}`}
      >
        <Bell size={20} />
        {unreadCount > 0 && (
          <span className="absolute top-1 right-1 w-4 h-4 bg-error text-surface text-[10px] font-bold rounded-full flex items-center justify-center border-2 border-background">
            {unreadCount}
          </span>
        )}
      </button>

      <AnimatePresence>
        {isOpen && (
          <motion.div 
            initial={{ opacity: 0, y: 10, scale: 0.95 }}
            animate={{ opacity: 1, y: 0, scale: 1 }}
            exit={{ opacity: 0, y: 10, scale: 0.95 }}
            transition={{ duration: 0.2, ease: "easeOut" }}
            className="absolute right-0 mt-3 w-80 md:w-96 bg-surface rounded-2xl shadow-xl shadow-primary/5 border border-outline/20 overflow-hidden z-50 origin-top-right"
          >
            <div className="p-4 border-b border-outline/10 flex justify-between items-center bg-surface">
              <h3 className="font-bold text-text">Notificaciones</h3>
              <button 
                onClick={markAllRead}
                className="text-xs font-semibold text-primary hover:underline"
              >
                Marcar todas como leídas
              </button>
            </div>

            <div className="max-h-[400px] overflow-y-auto bg-surface">
              {notifications.length > 0 ? (
                <div className="divide-y divide-outline/5">
                  {notifications.map((n) => (
                    <div 
                      key={n.id} 
                      className={`p-4 hover:bg-surface-low transition-colors cursor-pointer flex gap-3 ${!n.read ? 'bg-primary/5' : ''}`}
                    >
                      <div className={`mt-0.5 w-8 h-8 rounded-full flex items-center justify-center shrink-0 ${
                        n.type === 'alert' ? 'bg-error/10 text-error' : 
                        n.type === 'success' ? 'bg-primary/10 text-primary' : 
                        'bg-tertiary/10 text-tertiary'
                      }`}>
                        {n.type === 'alert' && <AlertTriangle size={16} />}
                        {n.type === 'success' && <CheckCircle size={16} />}
                        {n.type === 'info' && <Info size={16} />}
                      </div>
                      <div className="flex-1 space-y-1">
                        <div className="flex justify-between items-start">
                          <p className={`text-sm font-bold ${!n.read ? 'text-text' : 'text-text-variant'}`}>
                            {n.title}
                          </p>
                          <span className="text-[10px] text-text-muted flex items-center gap-1">
                            <Clock size={10} /> {n.time}
                          </span>
                        </div>
                        <p className="text-xs text-text-variant leading-relaxed">
                          {n.message}
                        </p>
                      </div>
                      {!n.read && (
                        <div className="mt-1.5 w-2 h-2 bg-primary rounded-full"></div>
                      )}
                    </div>
                  ))}
                </div>
              ) : (
                <div className="p-10 text-center space-y-2">
                  <div className="w-12 h-12 bg-surface-high rounded-full flex items-center justify-center mx-auto text-text-muted">
                    <Bell size={24} />
                  </div>
                  <p className="text-sm text-text-variant font-medium">No hay notificaciones nuevas</p>
                </div>
              )}
            </div>

            <div className="p-3 bg-surface-low border-t border-outline/10 text-center">
              <button className="text-xs font-bold text-text-muted hover:text-primary transition-colors">
                Ver todo el historial
              </button>
            </div>
          </motion.div>
        )}
      </AnimatePresence>
    </div>
  );
};
