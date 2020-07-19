import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import {MatCardModule} from '@angular/material/card';
import {MatFormFieldModule} from '@angular/material/form-field';
import {MatInputModule} from '@angular/material/input';
import {MatButtonModule} from '@angular/material/button';
import {MatIconModule} from '@angular/material/icon';
import {MatToolbarModule} from '@angular/material/toolbar';
import {MatListModule} from '@angular/material/list';
import {MatTabsModule} from '@angular/material/tabs';
import {MatSnackBarModule} from '@angular/material/snack-bar';
import {MatDividerModule} from '@angular/material/divider';
import {MatRippleModule} from '@angular/material/core';
import {MatDialogModule, MatDialogRef} from '@angular/material/dialog';
import {MatMenuModule} from '@angular/material/menu';
import {MatGridListModule} from '@angular/material/grid-list';


@NgModule({
  declarations: [],
  imports: [
    CommonModule,
    MatCardModule,
    MatFormFieldModule,
    MatInputModule,
    MatButtonModule,
    MatIconModule,
    MatToolbarModule,
    MatListModule,
    MatTabsModule,
    MatSnackBarModule,
    MatDividerModule,
    MatRippleModule,
    MatDialogModule,
    MatMenuModule,
    MatGridListModule
  ],
  exports: [
    MatCardModule,
    MatFormFieldModule,
    MatInputModule,
    MatButtonModule,
    MatIconModule,
    MatToolbarModule,
    MatListModule,
    MatTabsModule,
    MatSnackBarModule,
    MatDividerModule,
    MatRippleModule,
    MatDialogModule,
    MatMenuModule,
    MatGridListModule
  ],
  providers: [
    {
      provide: MatDialogRef,
      useValue: {}
    }
  ]
})
export class MaterialModule {}
