if (!import.meta.env.SPRING_BACKEND_ADDRESS) {
  throw new Error('SPRING_BACKEND_ADDRESS is not defined');
}

const loadEnv = () => {
  return {
    SPRING_BACKEND_ADDRESS:
        (import.meta.env.SPRING_BACKEND_ADDRESS as string) || 'http://localhost:8080/api',
  };
};

export default loadEnv;
