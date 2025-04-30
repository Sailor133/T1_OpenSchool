package com.sysoev.t1_openschool.dto.task.request;

public class TaskRequestDto {
        private String title;
        private String description;
        private Long userId;

        public String getTitle() {
                return title;
        }

        public String getDescription() {
                return description;
        }

        public Long getUserId() {
                return userId;
        }

        public void setTitle(String title) {
                this.title = title;
        }

        public void setDescription(String description) {
                this.description = description;
        }

        public void setUserId(Long userId) {
                this.userId = userId;
        }
}
