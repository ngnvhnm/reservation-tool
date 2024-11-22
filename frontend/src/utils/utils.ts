export const getColor = (status: string) => {
  switch (status) {
    case 'ongoing':
      return 'green';
    case 'finished':
      return 'blue';
    case 'cancelled':
      return 'red';
    default:
      return 'gray';
  }
};