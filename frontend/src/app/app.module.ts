import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { HttpClientModule } from '@angular/common/http';

import { AppComponent } from './app.component';
import { CarListComponent } from './car-list/car-list.component';
import { CarService } from './shared/car/car.service';

const appRoutes: Routes = [
  {path: '', redirectTo: '/', pathMatch: 'full'},
  {
    path: 'car-list',
    component: CarListComponent
  }
];



@NgModule({
  declarations: [
    AppComponent,

    CarListComponent
  ],
  imports: [
    BrowserModule,
    HttpClientModule,
    RouterModule.forRoot(appRoutes, { enableTracing: true,})
  ],
  exports: [
    RouterModule
  ],
  providers: [CarService],
  bootstrap: [AppComponent]
})
export class AppModule { }

