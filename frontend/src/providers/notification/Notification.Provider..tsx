import { Notifications } from '@mantine/notifications';
import React, { createContext } from 'react';

const NotificationContext = createContext<null>(null);

interface NotificationProviderProps {
  children: React.ReactNode;
}

const NotificationProvider = ({ children }: NotificationProviderProps) => {
  return (
    <NotificationContext.Provider value={null}>
      <div>
        <Notifications position="bottom-right" mt="xl" />
        {children}
      </div>
    </NotificationContext.Provider>
  );
};

export { NotificationProvider };
