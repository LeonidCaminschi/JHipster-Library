export interface IAuthor {
  id: number;
  firstName?: string | null;
  lastName?: string | null;
}

export type NewAuthor = Omit<IAuthor, 'id'> & { id: null };
