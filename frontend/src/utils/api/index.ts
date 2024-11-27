import loadEnv from '../load-env';
import keycloak from '../../providers/authentication/keycloak.ts';

export interface Message {
  success: boolean;
  message: string;
}

export const headers = async () => {
  const token = keycloak.idToken ?? 'No token found';
  if (!token) {
    console.warn('No token found');
  }
  return {
    'Content-Type': 'application/json',
    Authorization: `Bearer ${token}`,
  };
};

export const checkBackendAvailability = async () => {
  const response = await fetch(`${loadEnv().SPRING_BACKEND_ADDRESS}`, {
    headers: { ...(await headers()) },
  });

  if (!response.ok) {
    throw new Error('Backend is not running');
  }
};
