import { Route, RouteProps, Routes, Navigate } from "react-router-dom";

import { BookingsPage } from './pages/BookingsPage.tsx';
import { SettingsPage } from './pages/SettingsPage.tsx';
import { HistoryPage } from './pages/HistoryPage.tsx';


export type RouteConfig = RouteProps & { path: string; isPrivate?: boolean };

const routes: RouteConfig[] = [
  {
    path: "/history",
    isPrivate: false,
    element: <HistoryPage />,
  },
  {
    path: "/booking",
    isPrivate: false,
    element: <BookingsPage />,
  },
  {
    path: "/",
    isPrivate: false,
    element: <Navigate to="/booking" />,
    index: true,
  },

  {
    path: "/settings",
    isPrivate: false,
    element: <SettingsPage />,
  }
];
const renderRouteMap = ({ element, ...restRoute }: RouteConfig) => {
  return <Route key={restRoute.path} element={element} {...restRoute} />;
};

export const AppRoutes = () => {
  return (
    <>
      <Routes>{routes.map(renderRouteMap)}</Routes>
    </>
  );
};
