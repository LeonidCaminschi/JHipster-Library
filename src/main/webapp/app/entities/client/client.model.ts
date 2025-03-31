export interface IClient {
  id: number;
  firstName?: string | null;
  lastName?: string | null;
  address?: string | null;
  phone?: string | null;
}

export type NewClient = Omit<IClient, 'id'> & { id: null };
