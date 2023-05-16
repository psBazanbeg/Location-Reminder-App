package com.example.mylocationapp;

public class ToDo {

        private int id;
        private  String title,about,username,password;
        public ToDo(){

        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }



        public String getAbout() {
            return about;
        }

        public void setAbout(String about) {
            this.about = about;
        }

        public ToDo(int id, String title, String about) {
            this.id = id;
            this.title = title;

            this.about = about;
        }

        public ToDo(String title,  String about) {
            this.title = title;

            this.about = about;
        }
}
