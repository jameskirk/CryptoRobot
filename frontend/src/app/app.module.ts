import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import {HTTP_INTERCEPTORS, HttpClientModule} from '@angular/common/http';

import { AppComponent } from './app.component';
import { CarListComponent } from './car-list/car-list.component';
import { CarService } from './shared/car/car.service';
import {AppService} from "app/app.service";
import {LoginComponent} from "./security/login.component";
import {FormsModule} from "@angular/forms";
import {JwtInterceptor} from "./jwt.interceptor";
import {LogoutComponent} from "./security/logout.component";

const appRoutes: Routes = [
  {path: '', redirectTo: '/', pathMatch: 'full'},
  {
    path: 'car-list',
    component: CarListComponent
  },
  { path: 'login', component: LoginComponent},
  { path: 'logout', component: LogoutComponent}
];



@NgModule({
  declarations: [
    AppComponent,
    LoginComponent,
    LogoutComponent,
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
  providers: [CarService, AppService, { provide: HTTP_INTERCEPTORS, useClass: JwtInterceptor, multi: true }],
  bootstrap: [AppComponent]
})
export class AppModule { }

