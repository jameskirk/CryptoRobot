import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { HttpClientModule } from '@angular/common/http';

import { AppComponent } from './app.component';
import { CarListComponent } from './car-list/car-list.component';
import { CarService } from './shared/car/car.service';
import {AppService} from "app/app.service";
import {LoginComponent} from "./security/login.component";
import {FormsModule} from "@angular/forms";

const appRoutes: Routes = [
  {path: '', redirectTo: '/', pathMatch: 'full'},
  {
    path: 'car-list',
    component: CarListComponent
  },
  { path: 'login', component: LoginComponent}
];



@NgModule({
  declarations: [
    AppComponent,
    LoginComponent,
    CarListComponent
  ],
  imports: [
    BrowserModule,
    HttpClientModule,
    RouterModule.forRoot(appRoutes, { enableTracing: true,}),
    FormsModule
  ],
  exports: [
    RouterModule
  ],
  providers: [CarService, AppService],
  bootstrap: [AppComponent]
})
export class AppModule { }

