import { ComponentFixture, TestBed } from '@angular/core/testing';

import { QuestionDetails } from './question-details';

describe('QuestionDetails', () => {
  let component: QuestionDetails;
  let fixture: ComponentFixture<QuestionDetails>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [QuestionDetails],
    }).compileComponents();

    fixture = TestBed.createComponent(QuestionDetails);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
