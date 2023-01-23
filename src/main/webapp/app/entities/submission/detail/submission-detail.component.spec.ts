import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { SubmissionDetailComponent } from './submission-detail.component';

describe('Submission Management Detail Component', () => {
  let comp: SubmissionDetailComponent;
  let fixture: ComponentFixture<SubmissionDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [SubmissionDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ submission: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(SubmissionDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(SubmissionDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load submission on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.submission).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
