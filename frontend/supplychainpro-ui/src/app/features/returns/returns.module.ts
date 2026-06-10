import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { SharedModule } from '../../shared/shared.module';

import { ReturnsRoutingModule } from './returns-routing.module';
import { ReturnsComponent } from './returns.component';


@NgModule({
  declarations: [
    ReturnsComponent
  ],
  imports: [
    CommonModule,
    SharedModule,
    ReturnsRoutingModule
  ]
})
export class ReturnsModule { }
