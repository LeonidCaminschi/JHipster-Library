import { IPublisher } from 'app/entities/publisher/publisher.model';

export interface IBook {
  id: number;
  name?: string | null;
  publishYear?: string | null;
  copies?: number | null;
  picture?: string | null;
  publisher?: IPublisher | null;
}

export type NewBook = Omit<IBook, 'id'> & { id: null };
