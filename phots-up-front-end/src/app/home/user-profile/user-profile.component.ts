import { Component, OnInit, ViewChild, ElementRef } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { UserInfo } from 'src/app/model/user-info';
import { UserInfoService } from 'src/app/shared/user-info.service';
import { AuthService } from 'src/app/shared/auth.service';
import { User } from 'src/app/model/user';

@Component({
  selector: 'app-user-profile',
  templateUrl: './user-profile.component.html',
  styleUrls: ['./user-profile.component.css']
})
export class UserProfileComponent implements OnInit {
  @ViewChild('avatarInput') avatarInput: ElementRef;
  avatar: File;
  readonly blankAvatar: string = 'assets/logo/blank.png';
  currUser: User;
  userProfile: User;
  aboutMe: string = '';
  canEdit: boolean = false;

  imgResult: string = '';

  isGoingEdit: boolean = false;

  constructor(
    private route: ActivatedRoute,
    private userService: UserInfoService,
    private auth: AuthService
    ) {}

  ngOnInit(): void {
    this.auth.getCurrUser()
      .subscribe(u => this.currUser = u);
    let userId = this.route.snapshot.queryParamMap.get('user');
    this.getUserInfo(userId);
  }


  private getUserInfo(userId: any){
    this.userService.getUserInfoByUserId(userId)
      .subscribe(user =>{
        if (!user.userInfo)
          user.userInfo = new UserInfo(-1, null, this.blankAvatar);
        if (!user.userInfo.avatarUrl)
          user.userInfo.avatarUrl = this.blankAvatar;
        this.userProfile = user;
        this.aboutMe = user.userInfo.aboutMe;
        this.canEdit = user.username === this.currUser.username;
      })
  }


  saveChanges(){
    let avaUrl = this.userProfile.userInfo.avatarUrl === this.blankAvatar ?
      null : this.userProfile.userInfo.avatarUrl;
    let newUserInfo = new UserInfo(null, this.aboutMe, avaUrl);
    
    this.userService.saveUserInfo(newUserInfo, this.avatar)
      .subscribe(resp =>{
        this.userProfile.userInfo.aboutMe = resp.body.userInfo.aboutMe;
        if (this.avatar){
          this.userProfile.userInfo.avatarUrl = this.imgResult;
          this.avatar = null;
        }
        this.isGoingEdit = false;
      })
  }

  loadAvatar(){
    if (this.canEdit)
      this.avatarInput.nativeElement.click();
  }

  showPreview(event: any){
    this.avatar = <File>event.target.files[0];

    if (this.avatar){
      let type = this.avatar.type;
      if (type.match(/image\/*/) == null){
        this.avatar = null;
        return;
      }

      const reader = new FileReader();
      reader.onload = ev => {
        this.imgResult = <string>ev.target.result;
        this.userProfile.userInfo.avatarUrl = this.imgResult;
      }

      reader.readAsDataURL(this.avatar);
      this.isGoingEdit=true;
    }
  }
  
  avatarUrl(): string{
	  return `background-image: url(${this.userProfile.userInfo.avatarUrl});`
  }

  onMouseOver(event){
    if (this.canEdit)
      event.target.style.cursor = 'pointer';
  }

  changeAboutMe(){
    if (this.canEdit && !this.isGoingEdit)
      this.isGoingEdit = true
  }
  onCancel(){
    this.isGoingEdit = false
  }
}
