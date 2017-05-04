package com.music.qiang.musicplayer.model;

import java.io.Serializable;
import java.util.List;

/**
 * 解析www.showapi.com/api中qq音乐免费接口的实体类，
 * 主要用到的是 {@link MusicOnlineBean.ShowapiResBodyBean.PagebeanBean.SonglistBeanX} 歌曲列表实体信息
 * <p>
 * Created by xuqiang on 2017/05/03.
 */

public class MusicOnlineBean implements Serializable {

    private static final long serialVersionUID = -9115320107115459164L;
    private int showapi_res_code;
    private String showapi_res_error;
    private ShowapiResBodyBean showapi_res_body;

    public int getShowapi_res_code() {
        return showapi_res_code;
    }

    public void setShowapi_res_code(int showapi_res_code) {
        this.showapi_res_code = showapi_res_code;
    }

    public String getShowapi_res_error() {
        return showapi_res_error;
    }

    public void setShowapi_res_error(String showapi_res_error) {
        this.showapi_res_error = showapi_res_error;
    }

    public ShowapiResBodyBean getShowapi_res_body() {
        return showapi_res_body;
    }

    public void setShowapi_res_body(ShowapiResBodyBean showapi_res_body) {
        this.showapi_res_body = showapi_res_body;
    }

    public static class ShowapiResBodyBean {

        private int ret_code;
        private PagebeanBean pagebean;

        public int getRet_code() {
            return ret_code;
        }

        public void setRet_code(int ret_code) {
            this.ret_code = ret_code;
        }

        public PagebeanBean getPagebean() {
            return pagebean;
        }

        public void setPagebean(PagebeanBean pagebean) {
            this.pagebean = pagebean;
        }

        public static class PagebeanBean {

            private int total_song_num;
            private int ret_code;
            private String update_time;
            private int color;
            private int cur_song_num;
            private int comment_num;
            private int currentPage;
            private int code;
            private int song_begin;
            private int totalpage;
            private CommentVoiceBean commentVoice;
            private List<SonglistBeanX> songlist;

            public int getTotal_song_num() {
                return total_song_num;
            }

            public void setTotal_song_num(int total_song_num) {
                this.total_song_num = total_song_num;
            }

            public int getRet_code() {
                return ret_code;
            }

            public void setRet_code(int ret_code) {
                this.ret_code = ret_code;
            }

            public String getUpdate_time() {
                return update_time;
            }

            public void setUpdate_time(String update_time) {
                this.update_time = update_time;
            }

            public int getColor() {
                return color;
            }

            public void setColor(int color) {
                this.color = color;
            }

            public int getCur_song_num() {
                return cur_song_num;
            }

            public void setCur_song_num(int cur_song_num) {
                this.cur_song_num = cur_song_num;
            }

            public int getComment_num() {
                return comment_num;
            }

            public void setComment_num(int comment_num) {
                this.comment_num = comment_num;
            }

            public int getCurrentPage() {
                return currentPage;
            }

            public void setCurrentPage(int currentPage) {
                this.currentPage = currentPage;
            }

            public int getCode() {
                return code;
            }

            public void setCode(int code) {
                this.code = code;
            }

            public int getSong_begin() {
                return song_begin;
            }

            public void setSong_begin(int song_begin) {
                this.song_begin = song_begin;
            }

            public int getTotalpage() {
                return totalpage;
            }

            public void setTotalpage(int totalpage) {
                this.totalpage = totalpage;
            }

            public CommentVoiceBean getCommentVoice() {
                return commentVoice;
            }

            public void setCommentVoice(CommentVoiceBean commentVoice) {
                this.commentVoice = commentVoice;
            }

            public List<SonglistBeanX> getSonglist() {
                return songlist;
            }

            public void setSonglist(List<SonglistBeanX> songlist) {
                this.songlist = songlist;
            }

            public static class CommentVoiceBean {

                private int total_song_num;
                private int color;
                private int cur_song_num;
                private int song_begin;
                private List<SonglistBean> songlist;

                public int getTotal_song_num() {
                    return total_song_num;
                }

                public void setTotal_song_num(int total_song_num) {
                    this.total_song_num = total_song_num;
                }

                public int getColor() {
                    return color;
                }

                public void setColor(int color) {
                    this.color = color;
                }

                public int getCur_song_num() {
                    return cur_song_num;
                }

                public void setCur_song_num(int cur_song_num) {
                    this.cur_song_num = cur_song_num;
                }

                public int getSong_begin() {
                    return song_begin;
                }

                public void setSong_begin(int song_begin) {
                    this.song_begin = song_begin;
                }

                public List<SonglistBean> getSonglist() {
                    return songlist;
                }

                public void setSonglist(List<SonglistBean> songlist) {
                    this.songlist = songlist;
                }

                public static class SonglistBean {

                    private String title;
                    private String mb;
                    private DataBean data;
                    private VidBean vid;

                    public String getTitle() {
                        return title;
                    }

                    public void setTitle(String title) {
                        this.title = title;
                    }

                    public String getMb() {
                        return mb;
                    }

                    public void setMb(String mb) {
                        this.mb = mb;
                    }

                    public DataBean getData() {
                        return data;
                    }

                    public void setData(DataBean data) {
                        this.data = data;
                    }

                    public VidBean getVid() {
                        return vid;
                    }

                    public void setVid(VidBean vid) {
                        this.vid = vid;
                    }

                    public static class DataBean {
                        /**
                         * albumdesc :
                         * stream : 10
                         * interval : 2870
                         * size320 : 114837650
                         * singer : [{"id":198895,"name":"QQ音乐","mid":"000xVBxt1xgiyW"}]
                         * isonly : 0
                         * switch : 599863
                         * albumname : QQ音乐巅峰榜
                         * type : 0
                         * rate : 7
                         * sizeape : 0
                         * songorig : QQ音乐巅峰榜2017年第16周
                         * albummid : 0003zHL824e14G
                         * pay : {"paydownload":0,"payplay":0,"timefree":0,"payalbum":0,"paytrackprice":0,"payinfo":0,"paytrackmouth":0,"payalbumprice":0}
                         * size128 : 45935198
                         * sizeflac : 0
                         * vid :
                         * albumid : 775950
                         * msgid : 0
                         * songtype : 0
                         * songid : 201954046
                         * label : 0
                         * sizeogg : 63635859
                         * cdIdx : 0
                         * songname : QQ音乐巅峰榜2017年第16周 (节目)
                         * preview : {"trybegin":0,"tryend":0,"trysize":0}
                         * alertid : 11
                         * strMediaMid : 003KHJTN3dt0dh
                         * songmid : 003KHJTN3dt0dh
                         * size5_1 : 0
                         * belongCD : 138
                         */

                        private String albumdesc;
                        private int stream;
                        private int interval;
                        private int size320;
                        private int isonly;
                        @com.google.gson.annotations.SerializedName("switch")
                        private int switchX;
                        private String albumname;
                        private int type;
                        private int rate;
                        private int sizeape;
                        private String songorig;
                        private String albummid;
                        private PayBean pay;
                        private int size128;
                        private int sizeflac;
                        private String vid;
                        private int albumid;
                        private int msgid;
                        private int songtype;
                        private int songid;
                        private String label;
                        private int sizeogg;
                        private int cdIdx;
                        private String songname;
                        private PreviewBean preview;
                        private int alertid;
                        private String strMediaMid;
                        private String songmid;
                        private int size5_1;
                        private int belongCD;
                        private List<SingerBean> singer;

                        public String getAlbumdesc() {
                            return albumdesc;
                        }

                        public void setAlbumdesc(String albumdesc) {
                            this.albumdesc = albumdesc;
                        }

                        public int getStream() {
                            return stream;
                        }

                        public void setStream(int stream) {
                            this.stream = stream;
                        }

                        public int getInterval() {
                            return interval;
                        }

                        public void setInterval(int interval) {
                            this.interval = interval;
                        }

                        public int getSize320() {
                            return size320;
                        }

                        public void setSize320(int size320) {
                            this.size320 = size320;
                        }

                        public int getIsonly() {
                            return isonly;
                        }

                        public void setIsonly(int isonly) {
                            this.isonly = isonly;
                        }

                        public int getSwitchX() {
                            return switchX;
                        }

                        public void setSwitchX(int switchX) {
                            this.switchX = switchX;
                        }

                        public String getAlbumname() {
                            return albumname;
                        }

                        public void setAlbumname(String albumname) {
                            this.albumname = albumname;
                        }

                        public int getType() {
                            return type;
                        }

                        public void setType(int type) {
                            this.type = type;
                        }

                        public int getRate() {
                            return rate;
                        }

                        public void setRate(int rate) {
                            this.rate = rate;
                        }

                        public int getSizeape() {
                            return sizeape;
                        }

                        public void setSizeape(int sizeape) {
                            this.sizeape = sizeape;
                        }

                        public String getSongorig() {
                            return songorig;
                        }

                        public void setSongorig(String songorig) {
                            this.songorig = songorig;
                        }

                        public String getAlbummid() {
                            return albummid;
                        }

                        public void setAlbummid(String albummid) {
                            this.albummid = albummid;
                        }

                        public PayBean getPay() {
                            return pay;
                        }

                        public void setPay(PayBean pay) {
                            this.pay = pay;
                        }

                        public int getSize128() {
                            return size128;
                        }

                        public void setSize128(int size128) {
                            this.size128 = size128;
                        }

                        public int getSizeflac() {
                            return sizeflac;
                        }

                        public void setSizeflac(int sizeflac) {
                            this.sizeflac = sizeflac;
                        }

                        public String getVid() {
                            return vid;
                        }

                        public void setVid(String vid) {
                            this.vid = vid;
                        }

                        public int getAlbumid() {
                            return albumid;
                        }

                        public void setAlbumid(int albumid) {
                            this.albumid = albumid;
                        }

                        public int getMsgid() {
                            return msgid;
                        }

                        public void setMsgid(int msgid) {
                            this.msgid = msgid;
                        }

                        public int getSongtype() {
                            return songtype;
                        }

                        public void setSongtype(int songtype) {
                            this.songtype = songtype;
                        }

                        public int getSongid() {
                            return songid;
                        }

                        public void setSongid(int songid) {
                            this.songid = songid;
                        }

                        public String getLabel() {
                            return label;
                        }

                        public void setLabel(String label) {
                            this.label = label;
                        }

                        public int getSizeogg() {
                            return sizeogg;
                        }

                        public void setSizeogg(int sizeogg) {
                            this.sizeogg = sizeogg;
                        }

                        public int getCdIdx() {
                            return cdIdx;
                        }

                        public void setCdIdx(int cdIdx) {
                            this.cdIdx = cdIdx;
                        }

                        public String getSongname() {
                            return songname;
                        }

                        public void setSongname(String songname) {
                            this.songname = songname;
                        }

                        public PreviewBean getPreview() {
                            return preview;
                        }

                        public void setPreview(PreviewBean preview) {
                            this.preview = preview;
                        }

                        public int getAlertid() {
                            return alertid;
                        }

                        public void setAlertid(int alertid) {
                            this.alertid = alertid;
                        }

                        public String getStrMediaMid() {
                            return strMediaMid;
                        }

                        public void setStrMediaMid(String strMediaMid) {
                            this.strMediaMid = strMediaMid;
                        }

                        public String getSongmid() {
                            return songmid;
                        }

                        public void setSongmid(String songmid) {
                            this.songmid = songmid;
                        }

                        public int getSize5_1() {
                            return size5_1;
                        }

                        public void setSize5_1(int size5_1) {
                            this.size5_1 = size5_1;
                        }

                        public int getBelongCD() {
                            return belongCD;
                        }

                        public void setBelongCD(int belongCD) {
                            this.belongCD = belongCD;
                        }

                        public List<SingerBean> getSinger() {
                            return singer;
                        }

                        public void setSinger(List<SingerBean> singer) {
                            this.singer = singer;
                        }

                        public static class PayBean {
                            /**
                             * paydownload : 0
                             * payplay : 0
                             * timefree : 0
                             * payalbum : 0
                             * paytrackprice : 0
                             * payinfo : 0
                             * paytrackmouth : 0
                             * payalbumprice : 0
                             */

                            private int paydownload;
                            private int payplay;
                            private int timefree;
                            private int payalbum;
                            private int paytrackprice;
                            private int payinfo;
                            private int paytrackmouth;
                            private int payalbumprice;

                            public int getPaydownload() {
                                return paydownload;
                            }

                            public void setPaydownload(int paydownload) {
                                this.paydownload = paydownload;
                            }

                            public int getPayplay() {
                                return payplay;
                            }

                            public void setPayplay(int payplay) {
                                this.payplay = payplay;
                            }

                            public int getTimefree() {
                                return timefree;
                            }

                            public void setTimefree(int timefree) {
                                this.timefree = timefree;
                            }

                            public int getPayalbum() {
                                return payalbum;
                            }

                            public void setPayalbum(int payalbum) {
                                this.payalbum = payalbum;
                            }

                            public int getPaytrackprice() {
                                return paytrackprice;
                            }

                            public void setPaytrackprice(int paytrackprice) {
                                this.paytrackprice = paytrackprice;
                            }

                            public int getPayinfo() {
                                return payinfo;
                            }

                            public void setPayinfo(int payinfo) {
                                this.payinfo = payinfo;
                            }

                            public int getPaytrackmouth() {
                                return paytrackmouth;
                            }

                            public void setPaytrackmouth(int paytrackmouth) {
                                this.paytrackmouth = paytrackmouth;
                            }

                            public int getPayalbumprice() {
                                return payalbumprice;
                            }

                            public void setPayalbumprice(int payalbumprice) {
                                this.payalbumprice = payalbumprice;
                            }
                        }

                        public static class PreviewBean {
                            /**
                             * trybegin : 0
                             * tryend : 0
                             * trysize : 0
                             */

                            private int trybegin;
                            private int tryend;
                            private int trysize;

                            public int getTrybegin() {
                                return trybegin;
                            }

                            public void setTrybegin(int trybegin) {
                                this.trybegin = trybegin;
                            }

                            public int getTryend() {
                                return tryend;
                            }

                            public void setTryend(int tryend) {
                                this.tryend = tryend;
                            }

                            public int getTrysize() {
                                return trysize;
                            }

                            public void setTrysize(int trysize) {
                                this.trysize = trysize;
                            }
                        }

                        public static class SingerBean {
                            /**
                             * id : 198895
                             * name : QQ音乐
                             * mid : 000xVBxt1xgiyW
                             */

                            private int id;
                            private String name;
                            private String mid;

                            public int getId() {
                                return id;
                            }

                            public void setId(int id) {
                                this.id = id;
                            }

                            public String getName() {
                                return name;
                            }

                            public void setName(String name) {
                                this.name = name;
                            }

                            public String getMid() {
                                return mid;
                            }

                            public void setMid(String mid) {
                                this.mid = mid;
                            }
                        }
                    }

                    public static class VidBean {
                        /**
                         * Fvid :
                         * Fmv_id :
                         * Fstatus :
                         */

                        private String Fvid;
                        private String Fmv_id;
                        private String Fstatus;

                        public String getFvid() {
                            return Fvid;
                        }

                        public void setFvid(String Fvid) {
                            this.Fvid = Fvid;
                        }

                        public String getFmv_id() {
                            return Fmv_id;
                        }

                        public void setFmv_id(String Fmv_id) {
                            this.Fmv_id = Fmv_id;
                        }

                        public String getFstatus() {
                            return Fstatus;
                        }

                        public void setFstatus(String Fstatus) {
                            this.Fstatus = Fstatus;
                        }
                    }
                }
            }

            public static class SonglistBeanX {
                /**
                 * songname : 星辰
                 * seconds : 249
                 * albummid : 0002zEaL14XNM6
                 * songid : 201712321
                 * singerid : 6499
                 * albumpic_big : http://i.gtimg.cn/music/photo/mid_album_300/M/6/0002zEaL14XNM6.jpg
                 * albumpic_small : http://i.gtimg.cn/music/photo/mid_album_90/M/6/0002zEaL14XNM6.jpg
                 * downUrl : http://dl.stream.qqmusic.qq.com/201712321.mp3?vkey=8E8AFEFB94107BAADBBF1958CAA9F885F6048C9935CDC5038F619D305A3F13C211B09ECAA5CEB7ED38990CE9F699DB7DEAAE23BE9C11A941&guid=2718671044
                 * url : http://ws.stream.qqmusic.qq.com/201712321.m4a?fromtag=46
                 * singername : 张杰
                 * albumid : 1989292
                 */

                private String songname;
                private int seconds;
                private String albummid;
                private int songid;
                private int singerid;
                private String albumpic_big;
                private String albumpic_small;
                private String downUrl;
                private String url;
                private String singername;
                private int albumid;

                public String getSongname() {
                    return songname;
                }

                public void setSongname(String songname) {
                    this.songname = songname;
                }

                public int getSeconds() {
                    return seconds;
                }

                public void setSeconds(int seconds) {
                    this.seconds = seconds;
                }

                public String getAlbummid() {
                    return albummid;
                }

                public void setAlbummid(String albummid) {
                    this.albummid = albummid;
                }

                public int getSongid() {
                    return songid;
                }

                public void setSongid(int songid) {
                    this.songid = songid;
                }

                public int getSingerid() {
                    return singerid;
                }

                public void setSingerid(int singerid) {
                    this.singerid = singerid;
                }

                public String getAlbumpic_big() {
                    return albumpic_big;
                }

                public void setAlbumpic_big(String albumpic_big) {
                    this.albumpic_big = albumpic_big;
                }

                public String getAlbumpic_small() {
                    return albumpic_small;
                }

                public void setAlbumpic_small(String albumpic_small) {
                    this.albumpic_small = albumpic_small;
                }

                public String getDownUrl() {
                    return downUrl;
                }

                public void setDownUrl(String downUrl) {
                    this.downUrl = downUrl;
                }

                public String getUrl() {
                    return url;
                }

                public void setUrl(String url) {
                    this.url = url;
                }

                public String getSingername() {
                    return singername;
                }

                public void setSingername(String singername) {
                    this.singername = singername;
                }

                public int getAlbumid() {
                    return albumid;
                }

                public void setAlbumid(int albumid) {
                    this.albumid = albumid;
                }
            }
        }
    }
}
