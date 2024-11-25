import loadEnv from '../load-env';

export interface Message {
  success: boolean;
  message: string;
}

export const headers = async () => {
  const token = 'testToken';
  if (!token) {
    console.warn('No token found');
  }
  return {
    'Content-Type': 'application/json',
    // Authorization: `Bearer ${token}`, // TODO: Add keycloak token for authorization
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
