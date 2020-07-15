import { Component, OnInit, ViewChild, Inject } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { FormGroup, FormControl, Validators } from '@angular/forms';

@Component({
  selector: 'app-add-post',
  templateUrl: './add-post.component.html',
  styleUrls: ['./add-post.component.css']
})
export class AddPostComponent implements OnInit {
  @ViewChild('imageInput') imageInput;
  imageData: File;
  imagePreview: string;

  post: FormGroup = new FormGroup({
    content: new FormControl('', Validators.required),
    image: new FormControl(null, Validators.required)
  });

  constructor(
    @Inject(MAT_DIALOG_DATA) private data: any,
    private ref: MatDialogRef<AddPostComponent>
    ) { }

  ngOnInit(): void {
    if (this.data){
      this.post.get('content').patchValue(`post id is ${this.data.postId}`)
    }
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
    
  }
}
