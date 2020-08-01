
export class StringUtils {

    public static getImageString64(imageKey: string, image: string): string{
        let index = imageKey.lastIndexOf('.');
        let extension = imageKey.substring(index + 1);
        return `data:image/${extension};base64,` + image;
    }
}
