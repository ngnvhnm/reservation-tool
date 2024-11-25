const isISO8601DateString = (s: string) => {
  const regex = /^\d{4}-\d{2}-\d{2}T\d{2}:\d{2}:\d{2}\.\d{3}Z$/;
  return regex.test(s);
};

const entityParser = <T>(entity: T): T => {
  if (Array.isArray(entity)) {
    return entity.map((item: unknown) => entityParser(item)) as unknown as T;
  }

  const parsedEntity: Partial<Record<keyof T, unknown>> = { ...entity };

  Object.keys(parsedEntity).forEach((key) => {
    const value = parsedEntity[key as keyof T];

    if (typeof value === 'string' && isISO8601DateString(value)) {
      // If the value is a string that represents a date, then convert it to a Date object with local timezone
      parsedEntity[key as keyof T] = new Date(value);
    } else if (typeof value === 'object' && value !== null) {
      // If the value is an object, then parse it recursively
      parsedEntity[key as keyof T] = entityParser(value);
    } else if (Array.isArray(value)) {
      // If the value is an array, then parse it recursively
      parsedEntity[key as keyof T] = value.map((item: unknown) => entityParser(item));
    }
  });
  return parsedEntity as T;
};

export default entityParser;
