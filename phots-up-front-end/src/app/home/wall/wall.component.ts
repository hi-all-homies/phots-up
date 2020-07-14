import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-wall',
  templateUrl: './wall.component.html',
  styleUrls: ['./wall.component.css']
})
export class WallComponent implements OnInit {
  posts: number[] = [1,2];
  liked: boolean = false;

  constructor() { }

  ngOnInit(): void {
  }

  onLike(){
    this.liked = !this.liked;
  }

}
