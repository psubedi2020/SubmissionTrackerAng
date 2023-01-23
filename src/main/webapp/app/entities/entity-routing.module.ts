import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'location',
        data: { pageTitle: 'submissionTrackerAngApp.location.home.title' },
        loadChildren: () => import('./location/location.module').then(m => m.LocationModule),
      },
      {
        path: 'candidate',
        data: { pageTitle: 'submissionTrackerAngApp.candidate.home.title' },
        loadChildren: () => import('./candidate/candidate.module').then(m => m.CandidateModule),
      },
      {
        path: 'submission',
        data: { pageTitle: 'submissionTrackerAngApp.submission.home.title' },
        loadChildren: () => import('./submission/submission.module').then(m => m.SubmissionModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
