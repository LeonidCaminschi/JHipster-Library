import { IAuthor } from 'app/entities/author/author.model';
import { IBook } from 'app/entities/book/book.model';

export interface IBookAuthor {
  id: number;
  author?: IAuthor | null;
  book?: IBook | null;
}

export type NewBookAuthor = Omit<IBookAuthor, 'id'> & { id: null };
