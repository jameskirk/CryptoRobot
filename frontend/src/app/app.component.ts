import { Component } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Router} from "@angular/router";
import {AppService} from "./app.service";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html'
})
export class AppComponent {

  constructor(public app: AppService, private http: HttpClient, private router: Router) {
  }

  logout() {
    console.log("logout");
    this.app.logout();
  }
}
