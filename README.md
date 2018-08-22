# Cordova Plugin for SC Mobile PayKey #
SC Mobile專用的 paykey

## Android ##

### 安裝方法 ###
`cordova plugin add cordova-plugin-sc-mobile-paykey --force`

### 注意事項 ###
對方廠商開發app的角度是單一app, 因此有很多變數沒有考慮別人會拿去用. 例如 
* 字串都存在 strings.xml
* 第一個進入點一定是MainActivity
* 第一個排版一定是activity_main.xml
* R 就是paykey的R, 不會是別人的R  

這些開發習慣會成為整合上的障礙, 現在要做的就是換名稱.

### 前提 ###
* SC Mobile的package是 **com.scb.com.tw**
* paykey唯一有extends Application的JAVA檔案是 **Sample.java**

### 整合步驟 ###

1. import org.paykey.keyboard.sample.SampleApp 替換成 import com.scb.mb.tw.SampleApp

2. import org.paykey.keyboard.sample.R 替換成 import com.scb.mb.tw.R

3. MainActivity.java 重新命名成 PayKeyActivity.java

4. colors.xml 重新命名成 paykey-colors.xml
5. dimers.xml 重新命名成 paykey-dimens.xml
6. styles.xml 重新命名成 paykey-styles.xml
7. 所有的strings.xml 整合成一個 paykey-values.xml
8. drawable, layout 資料夾內檢查有沒有名稱相同的, 記得名字改掉.
9. **修改config.xml 將有更名的地方重複做檢查**