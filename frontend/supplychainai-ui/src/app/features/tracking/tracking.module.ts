import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { SharedModule } from '../../shared/shared.module';

import { TrackingRoutingModule } from './tracking-routing.module';
import { TrackingComponent } from './tracking.component';


@NgModule({
  declarations: [
    TrackingComponent
  ],
  imports: [
    CommonModule,
    SharedModule,
    TrackingRoutingModule
  ]
})
export class TrackingModule { }
