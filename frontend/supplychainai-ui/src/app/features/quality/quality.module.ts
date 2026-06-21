import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { SharedModule } from '../../shared/shared.module';

import { QualityRoutingModule } from './quality-routing.module';
import { QualityComponent } from './quality.component';


@NgModule({
  declarations: [
    QualityComponent
  ],
  imports: [
    CommonModule,
    SharedModule,
    QualityRoutingModule
  ]
})
export class QualityModule { }
