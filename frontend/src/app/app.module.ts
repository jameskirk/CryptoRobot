import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import {HTTP_INTERCEPTORS, HttpClientModule} from '@angular/common/http';

import { AppComponent } from './app.component';
import { CarListComponent } from './car-list/car-list.component';
import { CarService } from './car-list/car.service';
import {AppService} from "app/app.service";
import {LoginComponent} from "./security/login.component";
import {FormsModule} from "@angular/forms";
import {JwtInterceptor} from "./jwt.interceptor";
import {LogoutComponent} from "./security/logout.component";
import {AuthGuard} from "./auth.guard";
import {TradeComponent} from "./trade/trade.component";
import {HomeComponent} from "./misc/home.component";

const appRoutes: Routes = [
  {path: '', component: HomeComponent, pathMatch: 'full'},
  { path: 'car-list', canActivate: [AuthGuard], component: CarListComponent },
  { path: 'login', component: LoginComponent},
  { path: 'logout', component: LogoutComponent},
  { path: 'trade', component: TradeComponent, pathMatch: 'full'},
  { path: 'trade/:exchange/:currency1/:currency2', component: TradeComponent, pathMatch: 'full'}
];



@NgModule({
  declarations: [
    AppComponent,
    HomeComponent,
    LoginComponent,
    LogoutComponent,
    CarListComponent,
    TradeComponent
  ],
  imports: [
    BrowserModule,
    HttpClientModule,
    RouterModule.forRoot(appRoutes, { enableTracing: false}),
    FormsModule
  ],
  exports: [
    RouterModule
  ],
  providers: [CarService, AppService, { provide: HTTP_INTERCEPTORS, useClass: JwtInterceptor, multi: true }, AuthGuard],
  bootstrap: [AppComponent]
})
export class AppModule { }

