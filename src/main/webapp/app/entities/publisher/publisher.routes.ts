import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import PublisherResolve from './route/publisher-routing-resolve.service';

const publisherRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/publisher.component').then(m => m.PublisherComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/publisher-detail.component').then(m => m.PublisherDetailComponent),
    resolve: {
      publisher: PublisherResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/publisher-update.component').then(m => m.PublisherUpdateComponent),
    resolve: {
      publisher: PublisherResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/publisher-update.component').then(m => m.PublisherUpdateComponent),
    resolve: {
      publisher: PublisherResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default publisherRoute;
