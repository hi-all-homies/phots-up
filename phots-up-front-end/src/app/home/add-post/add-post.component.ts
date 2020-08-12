import { Component, OnInit, ViewChild, Inject } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { FormGroup, FormControl, Validators } from '@angular/forms';
import { PostService } from 'src/app/shared/post.service';
import { Post } from 'src/app/model/post';
import { PostSummary } from 'src/app/model/post-summary';

@Component({
  selector: 'app-add-post',
  templateUrl: './add-post.component.html',
  styleUrls: ['./add-post.component.css']
})
export class AddPostComponent implements OnInit {
  @ViewChild('imageInput') imageInput;
  imageData: File;
  imagePreview: string;
  isSending: boolean = false;

  post: FormGroup = new FormGroup({
    content: new FormControl('', Validators.required),
    image: new FormControl(null, Validators.required)
  });

  isEdit: boolean = false;

  constructor(
    @Inject(MAT_DIALOG_DATA) private data: any,
    private ref: MatDialogRef<AddPostComponent>,
    private postService: PostService
    ) { }

  ngOnInit(): void {
    if (this.data)
      this.initForEdit();
  }

  private initForEdit(){
    this.post.get('content').patchValue(this.data.postSumm.post.content);
    this.imagePreview = this.data.postSumm.image;
    this.isEdit = this.data.isEdit;
    this.post.get('image').patchValue(this.imagePreview.length);
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

  edit(){
	this.isSending = true;
    this.data.postSumm.post.content = this.post.get('content').value;
    this.data.postSumm.image = this.imagePreview;
    this.postService.updatePost(this.data.postSumm.post, this.imageData)
      .subscribe(resp => this.ref.close());
  }

  send(){
    this.isSending = true;
    let post = new Post();
    post.content = this.post.get('content').value;
    this.postService.publishPost(post, this.imageData)
      .subscribe(p =>
        this.ref.close(this.gatherPostSummary(p)));
  }

  private gatherPostSummary(post: Post): PostSummary{
    return {
      post: post,
      meLiked: false,
      image: this.imagePreview,
      comments: 0,
      likes:0
    };
  }
}
