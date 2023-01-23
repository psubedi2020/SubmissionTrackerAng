import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { SubmissionComponent } from '../list/submission.component';
import { SubmissionDetailComponent } from '../detail/submission-detail.component';
import { SubmissionUpdateComponent } from '../update/submission-update.component';
import { SubmissionRoutingResolveService } from './submission-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const submissionRoute: Routes = [
  {
    path: '',
    component: SubmissionComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: SubmissionDetailComponent,
    resolve: {
      submission: SubmissionRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: SubmissionUpdateComponent,
    resolve: {
      submission: SubmissionRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: SubmissionUpdateComponent,
    resolve: {
      submission: SubmissionRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(submissionRoute)],
  exports: [RouterModule],
})
export class SubmissionRoutingModule {}
