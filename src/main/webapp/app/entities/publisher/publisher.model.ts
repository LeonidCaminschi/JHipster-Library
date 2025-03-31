export interface IPublisher {
  id: number;
  name?: string | null;
}

export type NewPublisher = Omit<IPublisher, 'id'> & { id: null };
