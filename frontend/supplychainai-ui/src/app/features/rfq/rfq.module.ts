import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { SharedModule } from '../../shared/shared.module';

import { RfqRoutingModule } from './rfq-routing.module';
import { RfqComponent } from './rfq.component';


@NgModule({
  declarations: [
    RfqComponent
  ],
  imports: [
    CommonModule,
    SharedModule,
    RfqRoutingModule
  ]
})
export class RfqModule { }
