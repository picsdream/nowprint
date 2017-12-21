package com.picsdream.picsdreamsdk.model.network;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;


public class UploadPhotoResponse {

    @SerializedName("data")
    @Expose
    private Data data;
    @SerializedName("files")
    @Expose
    private Files files;

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public Files getFiles() {
        return files;
    }

    public void setFiles(Files files) {
        this.files = files;
    }

    public class Data {

        @SerializedName("order_no")
        @Expose
        private String orderNo;
        @SerializedName("ids")
        @Expose
        private List<String> ids = null;
        @SerializedName("invoice_id")
        @Expose
        private String invoiceId;
        @SerializedName("email")
        @Expose
        private String email;

        public String getOrderNo() {
            return orderNo;
        }

        public void setOrderNo(String orderNo) {
            this.orderNo = orderNo;
        }

        public List<String> getIds() {
            return ids;
        }

        public void setIds(List<String> ids) {
            this.ids = ids;
        }

        public String getInvoiceId() {
            return invoiceId;
        }

        public void setInvoiceId(String invoiceId) {
            this.invoiceId = invoiceId;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

    }

    public class Files {

        @SerializedName("media")
        @Expose
        private Media media;

        public Media getMedia() {
            return media;
        }

        public void setMedia(Media media) {
            this.media = media;
        }

        public class Media {

            @SerializedName("name")
            @Expose
            private List<String> name = null;
            @SerializedName("type")
            @Expose
            private List<String> type = null;
            @SerializedName("tmp_name")
            @Expose
            private List<String> tmpName = null;
            @SerializedName("error")
            @Expose
            private List<Integer> error = null;
            @SerializedName("size")
            @Expose
            private List<Integer> size = null;

            public List<String> getName() {
                return name;
            }

            public void setName(List<String> name) {
                this.name = name;
            }

            public List<String> getType() {
                return type;
            }

            public void setType(List<String> type) {
                this.type = type;
            }

            public List<String> getTmpName() {
                return tmpName;
            }

            public void setTmpName(List<String> tmpName) {
                this.tmpName = tmpName;
            }

            public List<Integer> getError() {
                return error;
            }

            public void setError(List<Integer> error) {
                this.error = error;
            }

            public List<Integer> getSize() {
                return size;
            }

            public void setSize(List<Integer> size) {
                this.size = size;
            }

        }

    }

}