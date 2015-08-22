package com.worker.pojo;

import java.util.Map;

/**
 * 对象化从文件中读取过来的用户12维度特征
 * 
 * @author seal
 **/
public class UserInfo {

	public String uid;
	
	public String gender;
	//用户最近照片的tag统计
	public Map<String,Double> lastedPicTag;
	//用户关注的人的tag统计
	public Map<String,Double> forwardPicTag;
	//用户分享的照片的tag统计
	public Map<String,Double> sharePicTag;
	//用户分享的照片专辑的tag统计
	public Map<String,Double> sharePicListTag;
	//用户分享的其他用户的照片tag统计
	public Map<String,Double> shareOtherPicTag;
	//用户订阅的照片专辑的tag统计
	public Map<String,Double> subscribePicListTag;
	//用户评论的照片的tag统计
	public Map<String,Double> commentPicTag;
	//用户点赞的照片的tag统计
	public Map<String,Double> prasiePicTag;
	//用户访问的照片的tag统计
	public Map<String,Double> visitPicTag;
	//用户访问的照片专辑的tag统计
	public Map<String,Double> visitPicListTag;
	
	public String getUid() {
		return uid;
	}
	public void setUid(String uid) {
		this.uid = uid;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public Map<String, Double> getLastedPicTag() {
		return lastedPicTag;
	}
	public void setLastedPicTag(Map<String, Double> lastedPicTag) {
		this.lastedPicTag = lastedPicTag;
	}
	public Map<String, Double> getForwardPicTag() {
		return forwardPicTag;
	}
	public void setForwardPicTag(Map<String, Double> forwardPicTag) {
		this.forwardPicTag = forwardPicTag;
	}
	public Map<String, Double> getSharePicTag() {
		return sharePicTag;
	}
	public void setSharePicTag(Map<String, Double> sharePicTag) {
		this.sharePicTag = sharePicTag;
	}
	public Map<String, Double> getSharePicListTag() {
		return sharePicListTag;
	}
	public void setSharePicListTag(Map<String, Double> sharePicListTag) {
		this.sharePicListTag = sharePicListTag;
	}
	public Map<String, Double> getShareOtherPicTag() {
		return shareOtherPicTag;
	}
	public void setShareOtherPicTag(Map<String, Double> shareOtherPicTag) {
		this.shareOtherPicTag = shareOtherPicTag;
	}
	public Map<String, Double> getSubscribePicListTag() {
		return subscribePicListTag;
	}
	public void setSubscribePicListTag(Map<String, Double> subscribePicListTag) {
		this.subscribePicListTag = subscribePicListTag;
	}
	public Map<String, Double> getCommentPicTag() {
		return commentPicTag;
	}
	public void setCommentPicTag(Map<String, Double> commentPicTag) {
		this.commentPicTag = commentPicTag;
	}
	public Map<String, Double> getPrasiePicTag() {
		return prasiePicTag;
	}
	public void setPrasiePicTag(Map<String, Double> prasiePicTag) {
		this.prasiePicTag = prasiePicTag;
	}
	public Map<String, Double> getVisitPicTag() {
		return visitPicTag;
	}
	public void setVisitPicTag(Map<String, Double> visitPicTag) {
		this.visitPicTag = visitPicTag;
	}
	public Map<String, Double> getVisitPicListTag() {
		return visitPicListTag;
	}
	public void setVisitPicListTag(Map<String, Double> visitPicListTag) {
		this.visitPicListTag = visitPicListTag;
	}
	
	/**
	 * 转换用户的文件读入流到运算格式
	 * 
	 * @param userInfo
	 */
	public UserInfo(String userInfo)
	{
		
	}
	
}
