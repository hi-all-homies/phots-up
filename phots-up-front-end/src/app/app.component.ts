import { Component } from '@angular/core';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  //notifications options
  options = {
    position: ['bottom', 'left'],
    lastOnBottom: false,
    timeOut: 5000,
    maxStack: 5,
    showProgressBar: false,
    pauseOnHover: false
  };
}
