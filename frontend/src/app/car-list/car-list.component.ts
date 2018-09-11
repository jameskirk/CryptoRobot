import { Component , OnInit} from '@angular/core';
import { CarService } from './car.service';

@Component({
  selector: 'app-car-list',
  templateUrl: './car-list.component.html'

})
export class CarListComponent implements OnInit {
  title = 'Cars page';
  cars: Array<any>;

  constructor(private carService: CarService) { }

  ngOnInit() {
    this.carService.getAll().subscribe(data => {
      this.cars = data;
      for (const car of this.cars) {
        console.log(`Car with id '${car.name}' not found, returning to list`);

      }
    });
  }
}
