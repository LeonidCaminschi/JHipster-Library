import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import BookAuthorResolve from './route/book-author-routing-resolve.service';

const bookAuthorRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/book-author.component').then(m => m.BookAuthorComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/book-author-detail.component').then(m => m.BookAuthorDetailComponent),
    resolve: {
      bookAuthor: BookAuthorResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/book-author-update.component').then(m => m.BookAuthorUpdateComponent),
    resolve: {
      bookAuthor: BookAuthorResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/book-author-update.component').then(m => m.BookAuthorUpdateComponent),
    resolve: {
      bookAuthor: BookAuthorResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default bookAuthorRoute;
