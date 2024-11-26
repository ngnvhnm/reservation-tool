import '@mantine/core/styles.css';
import '@mantine/dates/styles.css';
import '@mantine/notifications/styles.css';
import './App.css';
import { MantineProvider } from '@mantine/core';
import { BrowserRouter } from 'react-router-dom';
import { BaseLayout } from './layout/BaseLayout.tsx';
import { AppRoutes } from './AppRoutes.tsx';
import { NotificationProvider } from './providers/notification/Notification.Provider..tsx';

function App() {
  return (
    <MantineProvider>
      <NotificationProvider>
        <BrowserRouter>
          <BaseLayout>
            <AppRoutes />
          </BaseLayout>
        </BrowserRouter>
      </NotificationProvider>
    </MantineProvider>
  );
}

export default App;
