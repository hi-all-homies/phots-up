import { Component, OnInit, Inject, ElementRef } from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogRef} from '@angular/material/dialog';
import { User } from 'src/app/model/user';

@Component({
  selector: 'app-like-dialog',
  templateUrl: './like-dialog.component.html',
  styleUrls: ['./like-dialog.component.css']
})
export class LikeDialogComponent implements OnInit {
  users: User[];
  trigger: ElementRef;

  constructor(
    private dialogRef: MatDialogRef<LikeDialogComponent>,
    @Inject (MAT_DIALOG_DATA) private data: any
    ) {
    this.trigger = this.data.trigger;
    this.users = this.data.users;
  }

  ngOnInit(): void {
    this.setPosition();
  }

  private setPosition(){
    let rect = this.trigger.nativeElement.getBoundingClientRect();
    let position = {
      top: `${rect.top+50}px`,
      left: `${rect.left}px`
    };
    this.dialogRef.updateSize('12%','30%');
    this.dialogRef.updatePosition(position);
  }
}
