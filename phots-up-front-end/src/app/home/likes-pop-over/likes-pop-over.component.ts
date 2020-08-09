import { Component, OnInit, ViewChild, ElementRef, Input, AfterViewInit } from '@angular/core';
import { User } from 'src/app/model/user';
import { fromEvent, interval } from 'rxjs';
import { debounce } from 'rxjs/operators';

@Component({
  selector: 'likes-popover',
  templateUrl: './likes-pop-over.component.html',
  styleUrls: ['./likes-pop-over.component.css']
})
export class LikesPopOverComponent implements OnInit,AfterViewInit {
  @ViewChild('popOver', {static: true}) private popOver: ElementRef;
  @Input('users') users: User[];
  @Input('source') source: HTMLElement;
  
  private popOverFlag: boolean = false;
  
  constructor() {}

  ngOnInit(){
    if (this.users == null || this.source == null)
      throw new Error('props must be provided');
    
  }
  
  ngAfterViewInit(){
    this.addListenersToSource();
  }

  onEnter(){
    this.popOverFlag = true;    
  }

  onLeave(){
    this.popOverFlag = false;
    this.setDisplayProp('none');
  }

  private addListenersToSource(){
    this.source.addEventListener('mouseenter',
      event => this.onMouseEnter(event));

    this.source.addEventListener('mouseleave',
      event => this.onMouseLeave(event));
  }

  private onMouseEnter(event: MouseEvent){
    this.popOver.nativeElement.style.top = `${event.y-200}px`;
    this.popOver.nativeElement.style.left = `${event.x+50}px`;
    this.setDisplayProp('flex');
  }

  private onMouseLeave(event: MouseEvent){
    fromEvent(this.source, 'mouseleave')
      .pipe(
        debounce(() => interval(1500)))
      .subscribe(ev => {
        if (!this.popOverFlag)
          this.setDisplayProp('none')
      })
  }

  private setDisplayProp(value: string){
    this.popOver.nativeElement.style.display = value;
  }
}
