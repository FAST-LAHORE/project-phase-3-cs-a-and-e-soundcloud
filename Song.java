/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package afra_sound_cloud;

/**
 *
 * @author Afrasiyab
 */
public class Song {
    static int id;
    private String Title;
    private String Singer;
    private String Genre;
    private String Description;
    private String File_name;
    private String Uploader;
    private String Url;
    
    static{
        id=0 ; //database function
    }
    
    public Song(String title,String singer,String genre,String file,String uploader,String desc,String url){
        Title = title;
        Singer = singer;
        Genre = genre;
        Description = desc;
        File_name = file;
        Uploader = uploader;
        Url = url;
        id++;
    }
    
    public Song(String title,String singer,String genre,String file,String uploader,String url){
        Title = title;
        Singer = singer;
        Genre = genre;
        Description = "";
        File_name = file;
        Uploader = uploader;
        Url = url;
        id++;
    }
    
    public Song(String file){
         Title = "";
        Singer = "";
        Genre = "";
        Description = "";
        File_name = file;
        Uploader = "";
        Url = "";
        id++;
    }
    
    public String get_Url(){
        return Url;
    }
    public String get_Title(){
        return Title;
    }
    
    public String get_Singer(){
        return Singer;
    }
    
    public String get_Genre(){
        return Genre;
    }
   
    public String get_Description(){
        return Description;
    }
    
    public String get_File_name(){
        return File_name;
    }
    
    public int get_id(){
        return id;
    }
    
    public String get_uploader(){
        return Uploader;
    }
    
    public String toString(){
        return "Title : " + Title + "\nSinger : " + Singer + "\nGenre : " + Genre + "\nFile Name : " + File_name + "\nDescription : " +Description+"\nUploader"+Uploader+"\n Url : "+Url;
    }
}
