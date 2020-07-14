import { Component, OnInit, ViewChild } from '@angular/core';
import { MatDialogRef } from '@angular/material/dialog';
import { FormGroup, FormControl, Validators } from '@angular/forms';

@Component({
  selector: 'app-add-post',
  templateUrl: './add-post.component.html',
  styleUrls: ['./add-post.component.css']
})
export class AddPostComponent implements OnInit {
  @ViewChild('imageInput') imageInput;
  post: FormGroup;
  imageData: File;
  imagePreview: string;

  constructor(private ref: MatDialogRef<AddPostComponent>) { }

  ngOnInit(): void {
    this.post = new FormGroup({
      content: new FormControl('',Validators.required),
      image: new FormControl(null,Validators.required)
    });
  }

  chooseImage(){
    this.imageInput.nativeElement.click();
  }

  imageChoosen(inputEvent: any){
    this.imageData = <File>inputEvent.target.files[0];
    this.preview();
  }

  private preview(){
    let type = this.imageData.type;
    if (type.match(/image\/*/) == null) {
      return;
    }

    const reader = new FileReader();
    reader.readAsDataURL(this.imageData);
    reader.onload = event =>{
      this.imagePreview = <string>reader.result;
      this.post.get('image').patchValue(this.imagePreview.length);
    };
  }

  onSubmit(){
    console.log(this.post.controls)
  }

  onClose(){
    this.ref.close();
  }
}
