import dayjs from 'dayjs/esm';
import { IBook } from 'app/entities/book/book.model';
import { IClient } from 'app/entities/client/client.model';

export interface IBorrowedBook {
  id: number;
  borrowDate?: dayjs.Dayjs | null;
  book?: IBook | null;
  client?: IClient | null;
}

export type NewBorrowedBook = Omit<IBorrowedBook, 'id'> & { id: null };
