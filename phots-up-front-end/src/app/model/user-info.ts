import { User } from './user';

export class UserInfo {
    
    constructor(
        public id: number,
        public avatarKey: string,
        public aboutMe: string,
        public user: User,
        public avatar: string
    ){}
}
