-- ============================================
-- 最新手机商品数据 (约150条)
-- Category: 手机
-- ============================================

-- ======== Apple iPhone (25条) ========
INSERT INTO item.item (name, price, stock, image, category, brand, spec, sold, comment_count, is_ad, status, create_time, update_time, creater, updater) VALUES
('iPhone 16 Pro Max 256GB 原色钛金属', 999900, 500, 'https://example.com/images/iphone16-promax.jpg', '手机', 'Apple', '{"颜色":"原色钛金属","存储":"256GB","屏幕":"6.9英寸","芯片":"A18 Pro"}', 85231, 12680, TRUE, 1, NOW(), NOW(), 1, 1),
('iPhone 16 Pro Max 512GB 沙漠钛金属', 1199900, 380, 'https://example.com/images/iphone16-promax.jpg', '手机', 'Apple', '{"颜色":"沙漠钛金属","存储":"512GB","屏幕":"6.9英寸","芯片":"A18 Pro"}', 56210, 8921, FALSE, 1, NOW(), NOW(), 1, 1),
('iPhone 16 Pro Max 1TB 白色钛金属', 1399900, 200, 'https://example.com/images/iphone16-promax.jpg', '手机', 'Apple', '{"颜色":"白色钛金属","存储":"1TB","屏幕":"6.9英寸","芯片":"A18 Pro"}', 32145, 5640, FALSE, 1, NOW(), NOW(), 1, 1),
('iPhone 16 Pro 256GB 黑色钛金属', 899900, 600, 'https://example.com/images/iphone16-pro.jpg', '手机', 'Apple', '{"颜色":"黑色钛金属","存储":"256GB","屏幕":"6.3英寸","芯片":"A18 Pro"}', 72310, 10982, TRUE, 1, NOW(), NOW(), 1, 1),
('iPhone 16 Pro 512GB 蓝色钛金属', 1049900, 420, 'https://example.com/images/iphone16-pro.jpg', '手机', 'Apple', '{"颜色":"蓝色钛金属","存储":"512GB","屏幕":"6.3英寸","芯片":"A18 Pro"}', 45892, 7230, FALSE, 1, NOW(), NOW(), 1, 1),
('iPhone 16 Pro 1TB 原色钛金属', 1249900, 180, 'https://example.com/images/iphone16-pro.jpg', '手机', 'Apple', '{"颜色":"原色钛金属","存储":"1TB","屏幕":"6.3英寸","芯片":"A18 Pro"}', 20983, 3876, FALSE, 1, NOW(), NOW(), 1, 1),
('iPhone 16 256GB 群青色', 699900, 800, 'https://example.com/images/iphone16.jpg', '手机', 'Apple', '{"颜色":"群青色","存储":"256GB","屏幕":"6.1英寸","芯片":"A18"}', 112450, 19540, TRUE, 1, NOW(), NOW(), 1, 1),
('iPhone 16 512GB 深青色', 849900, 550, 'https://example.com/images/iphone16.jpg', '手机', 'Apple', '{"颜色":"深青色","存储":"512GB","屏幕":"6.1英寸","芯片":"A18"}', 67890, 10230, FALSE, 1, NOW(), NOW(), 1, 1),
('iPhone 16 Plus 256GB 粉色', 799900, 650, 'https://example.com/images/iphone16-plus.jpg', '手机', 'Apple', '{"颜色":"粉色","存储":"256GB","屏幕":"6.7英寸","芯片":"A18"}', 89765, 14320, FALSE, 1, NOW(), NOW(), 1, 1),
('iPhone 16 Plus 512GB 白色', 949900, 400, 'https://example.com/images/iphone16-plus.jpg', '手机', 'Apple', '{"颜色":"白色","存储":"512GB","屏幕":"6.7英寸","芯片":"A18"}', 54678, 8765, FALSE, 1, NOW(), NOW(), 1, 1),
('iPhone 15 Pro Max 256GB 原色钛金属', 849900, 350, 'https://example.com/images/iphone15-promax.jpg', '手机', 'Apple', '{"颜色":"原色钛金属","存储":"256GB","屏幕":"6.7英寸","芯片":"A17 Pro"}', 145230, 23560, FALSE, 1, NOW(), NOW(), 1, 1),
('iPhone 15 Pro Max 512GB 蓝色钛金属', 999900, 280, 'https://example.com/images/iphone15-promax.jpg', '手机', 'Apple', '{"颜色":"蓝色钛金属","存储":"512GB","屏幕":"6.7英寸","芯片":"A17 Pro"}', 98765, 15678, FALSE, 1, NOW(), NOW(), 1, 1),
('iPhone 15 Pro 256GB 白色钛金属', 749900, 420, 'https://example.com/images/iphone15-pro.jpg', '手机', 'Apple', '{"颜色":"白色钛金属","存储":"256GB","屏幕":"6.1英寸","芯片":"A17 Pro"}', 132450, 21340, FALSE, 1, NOW(), NOW(), 1, 1),
('iPhone 15 Pro 512GB 黑色钛金属', 899900, 310, 'https://example.com/images/iphone15-pro.jpg', '手机', 'Apple', '{"颜色":"黑色钛金属","存储":"512GB","屏幕":"6.1英寸","芯片":"A17 Pro"}', 87654, 14230, FALSE, 1, NOW(), NOW(), 1, 1),
('iPhone 15 256GB 粉色', 549900, 700, 'https://example.com/images/iphone15.jpg', '手机', 'Apple', '{"颜色":"粉色","存储":"256GB","屏幕":"6.1英寸","芯片":"A16"}', 167890, 28760, FALSE, 1, NOW(), NOW(), 1, 1),
('iPhone 15 512GB 绿色', 699900, 500, 'https://example.com/images/iphone15.jpg', '手机', 'Apple', '{"颜色":"绿色","存储":"512GB","屏幕":"6.1英寸","芯片":"A16"}', 102340, 16780, FALSE, 1, NOW(), NOW(), 1, 1),
('iPhone 15 Plus 256GB 蓝色', 649900, 550, 'https://example.com/images/iphone15-plus.jpg', '手机', 'Apple', '{"颜色":"蓝色","存储":"256GB","屏幕":"6.7英寸","芯片":"A16"}', 115670, 19340, FALSE, 1, NOW(), NOW(), 1, 1),
('iPhone 14 Pro Max 256GB 暗紫色', 749900, 200, 'https://example.com/images/iphone14-promax.jpg', '手机', 'Apple', '{"颜色":"暗紫色","存储":"256GB","屏幕":"6.7英寸","芯片":"A16"}', 189450, 32450, FALSE, 1, NOW(), NOW(), 1, 1),
('iPhone 14 Pro 256GB 金色', 649900, 280, 'https://example.com/images/iphone14-pro.jpg', '手机', 'Apple', '{"颜色":"金色","存储":"256GB","屏幕":"6.1英寸","芯片":"A16"}', 156780, 27890, FALSE, 1, NOW(), NOW(), 1, 1),
('iPhone 14 256GB 午夜色', 449900, 450, 'https://example.com/images/iphone14.jpg', '手机', 'Apple', '{"颜色":"午夜色","存储":"256GB","屏幕":"6.1英寸","芯片":"A15"}', 213450, 38960, FALSE, 1, NOW(), NOW(), 1, 1),
('iPhone SE 第4代 128GB 午夜色', 349900, 1000, 'https://example.com/images/iphonese4.jpg', '手机', 'Apple', '{"颜色":"午夜色","存储":"128GB","屏幕":"6.1英寸","芯片":"A18"}', 98765, 15678, FALSE, 1, NOW(), NOW(), 1, 1),
('iPhone SE 第4代 256GB 星光色', 429900, 750, 'https://example.com/images/iphonese4.jpg', '手机', 'Apple', '{"颜色":"星光色","存储":"256GB","屏幕":"6.1英寸","芯片":"A18"}', 65432, 10234, FALSE, 1, NOW(), NOW(), 1, 1),
('iPhone 16 Pro Max 256GB 沙漠钛金属 官方翻新', 849900, 50, 'https://example.com/images/iphone16-promax.jpg', '手机', 'Apple', '{"颜色":"沙漠钛金属","存储":"256GB","屏幕":"6.9英寸","芯片":"A18 Pro","备注":"官方翻新"}', 12340, 2340, FALSE, 1, NOW(), NOW(), 1, 1),
('iPhone 16 128GB 黑色', 599900, 900, 'https://example.com/images/iphone16.jpg', '手机', 'Apple', '{"颜色":"黑色","存储":"128GB","屏幕":"6.1英寸","芯片":"A18"}', 120345, 20123, FALSE, 1, NOW(), NOW(), 1, 1),
('iPhone 16e 256GB 黑色', 449900, 600, 'https://example.com/images/iphone16e.jpg', '手机', 'Apple', '{"颜色":"黑色","存储":"256GB","屏幕":"6.1英寸","芯片":"A18"}', 45678, 7890, FALSE, 1, NOW(), NOW(), 1, 1);

-- ======== iPhone 17 系列 (15条) ========
INSERT INTO item.item (name, price, stock, image, category, brand, spec, sold, comment_count, is_ad, status, create_time, update_time, creater, updater) VALUES
('iPhone 17 Pro Max 256GB 流光金', 1099900, 450, 'https://example.com/images/iphone17-promax.jpg', '手机', 'Apple', '{"颜色":"流光金","存储":"256GB","屏幕":"6.9英寸","芯片":"A19 Pro","网络":"5G"}', 76231, 11340, TRUE, 1, NOW(), NOW(), 1, 1),
('iPhone 17 Pro Max 512GB 暗夜紫', 1249900, 350, 'https://example.com/images/iphone17-promax.jpg', '手机', 'Apple', '{"颜色":"暗夜紫","存储":"512GB","屏幕":"6.9英寸","芯片":"A19 Pro","网络":"5G"}', 48210, 7560, FALSE, 1, NOW(), NOW(), 1, 1),
('iPhone 17 Pro Max 1TB 星河银', 1499900, 180, 'https://example.com/images/iphone17-promax.jpg', '手机', 'Apple', '{"颜色":"星河银","存储":"1TB","屏幕":"6.9英寸","芯片":"A19 Pro","网络":"5G"}', 28145, 4890, FALSE, 1, NOW(), NOW(), 1, 1),
('iPhone 17 Pro 256GB 深空蓝', 949900, 550, 'https://example.com/images/iphone17-pro.jpg', '手机', 'Apple', '{"颜色":"深空蓝","存储":"256GB","屏幕":"6.3英寸","芯片":"A19 Pro","网络":"5G"}', 64310, 9870, TRUE, 1, NOW(), NOW(), 1, 1),
('iPhone 17 Pro 512GB 星光金', 1099900, 380, 'https://example.com/images/iphone17-pro.jpg', '手机', 'Apple', '{"颜色":"星光金","存储":"512GB","屏幕":"6.3英寸","芯片":"A19 Pro","网络":"5G"}', 39872, 6340, FALSE, 1, NOW(), NOW(), 1, 1),
('iPhone 17 Pro 1TB 暮光紫', 1349900, 150, 'https://example.com/images/iphone17-pro.jpg', '手机', 'Apple', '{"颜色":"暮光紫","存储":"1TB","屏幕":"6.3英寸","芯片":"A19 Pro","网络":"5G"}', 18920, 3120, FALSE, 1, NOW(), NOW(), 1, 1),
('iPhone 17 256GB 冰雾蓝', 749900, 750, 'https://example.com/images/iphone17.jpg', '手机', 'Apple', '{"颜色":"冰雾蓝","存储":"256GB","屏幕":"6.1英寸","芯片":"A19","网络":"5G"}', 98780, 16540, TRUE, 1, NOW(), NOW(), 1, 1),
('iPhone 17 512GB 樱语粉', 899900, 500, 'https://example.com/images/iphone17.jpg', '手机', 'Apple', '{"颜色":"樱语粉","存储":"512GB","屏幕":"6.1英寸","芯片":"A19","网络":"5G"}', 58920, 9120, FALSE, 1, NOW(), NOW(), 1, 1),
('iPhone 17 128GB 午夜色', 649900, 850, 'https://example.com/images/iphone17.jpg', '手机', 'Apple', '{"颜色":"午夜色","存储":"128GB","屏幕":"6.1英寸","芯片":"A19","网络":"5G"}', 109870, 18760, FALSE, 1, NOW(), NOW(), 1, 1),
('iPhone 17 Plus 256GB 海沫绿', 849900, 600, 'https://example.com/images/iphone17-plus.jpg', '手机', 'Apple', '{"颜色":"海沫绿","存储":"256GB","屏幕":"6.7英寸","芯片":"A19","网络":"5G"}', 78920, 13240, FALSE, 1, NOW(), NOW(), 1, 1),
('iPhone 17 Plus 512GB 星光白', 999900, 380, 'https://example.com/images/iphone17-plus.jpg', '手机', 'Apple', '{"颜色":"星光白","存储":"512GB","屏幕":"6.7英寸","芯片":"A19","网络":"5G"}', 47650, 7650, FALSE, 1, NOW(), NOW(), 1, 1),
('iPhone 17 Air 256GB 极光银', 799900, 500, 'https://example.com/images/iphone17-air.jpg', '手机', 'Apple', '{"颜色":"极光银","存储":"256GB","屏幕":"6.6英寸","芯片":"A19","网络":"5G","备注":"超薄机身5.5mm"}', 52140, 8670, TRUE, 1, NOW(), NOW(), 1, 1),
('iPhone 17 Air 512GB 薄暮蓝', 949900, 320, 'https://example.com/images/iphone17-air.jpg', '手机', 'Apple', '{"颜色":"薄暮蓝","存储":"512GB","屏幕":"6.6英寸","芯片":"A19","网络":"5G","备注":"超薄机身5.5mm"}', 31230, 5340, FALSE, 1, NOW(), NOW(), 1, 1),
('iPhone 17e 256GB 星辰黑', 499900, 550, 'https://example.com/images/iphone17e.jpg', '手机', 'Apple', '{"颜色":"星辰黑","存储":"256GB","屏幕":"6.1英寸","芯片":"A19","网络":"5G"}', 39870, 6780, FALSE, 1, NOW(), NOW(), 1, 1);

-- ======== Samsung (30条) ========
INSERT INTO item.item (name, price, stock, image, category, brand, spec, sold, comment_count, is_ad, status, create_time, update_time, creater, updater) VALUES
('Samsung Galaxy S25 Ultra 256GB 钛灰色', 969900, 450, 'https://example.com/images/s25ultra.jpg', '手机', 'Samsung', '{"颜色":"钛灰色","存储":"256GB","屏幕":"6.9英寸","芯片":"骁龙8 Elite"}', 72340, 11560, TRUE, 1, NOW(), NOW(), 1, 1),
('Samsung Galaxy S25 Ultra 512GB 钛黑色', 1089900, 350, 'https://example.com/images/s25ultra.jpg', '手机', 'Samsung', '{"颜色":"钛黑色","存储":"512GB","屏幕":"6.9英寸","芯片":"骁龙8 Elite"}', 52109, 8723, FALSE, 1, NOW(), NOW(), 1, 1),
('Samsung Galaxy S25 Ultra 1TB 钛蓝色', 1289900, 150, 'https://example.com/images/s25ultra.jpg', '手机', 'Samsung', '{"颜色":"钛蓝色","存储":"1TB","屏幕":"6.9英寸","芯片":"骁龙8 Elite"}', 21567, 4089, FALSE, 1, NOW(), NOW(), 1, 1),
('Samsung Galaxy S25+ 256GB 幻夜黑', 749900, 550, 'https://example.com/images/s25plus.jpg', '手机', 'Samsung', '{"颜色":"幻夜黑","存储":"256GB","屏幕":"6.7英寸","芯片":"骁龙8 Elite"}', 48930, 7982, FALSE, 1, NOW(), NOW(), 1, 1),
('Samsung Galaxy S25+ 512GB 雪雾白', 899900, 380, 'https://example.com/images/s25plus.jpg', '手机', 'Samsung', '{"颜色":"雪雾白","存储":"512GB","屏幕":"6.7英寸","芯片":"骁龙8 Elite"}', 30456, 5467, FALSE, 1, NOW(), NOW(), 1, 1),
('Samsung Galaxy S25 256GB 薄荷绿', 649900, 700, 'https://example.com/images/s25.jpg', '手机', 'Samsung', '{"颜色":"薄荷绿","存储":"256GB","屏幕":"6.2英寸","芯片":"骁龙8 Elite"}', 56430, 9540, TRUE, 1, NOW(), NOW(), 1, 1),
('Samsung Galaxy S25 512GB 暖橙色', 799900, 500, 'https://example.com/images/s25.jpg', '手机', 'Samsung', '{"颜色":"暖橙色","存储":"512GB","屏幕":"6.2英寸","芯片":"骁龙8 Elite"}', 38720, 6230, FALSE, 1, NOW(), NOW(), 1, 1),
('Samsung Galaxy S24 Ultra 256GB 钛灰色', 799900, 320, 'https://example.com/images/s24ultra.jpg', '手机', 'Samsung', '{"颜色":"钛灰色","存储":"256GB","屏幕":"6.8英寸","芯片":"骁龙8 Gen3"}', 115670, 21230, FALSE, 1, NOW(), NOW(), 1, 1),
('Samsung Galaxy S24 Ultra 512GB 钛黑色', 949900, 240, 'https://example.com/images/s24ultra.jpg', '手机', 'Samsung', '{"颜色":"钛黑色","存储":"512GB","屏幕":"6.8英寸","芯片":"骁龙8 Gen3"}', 78930, 14560, FALSE, 1, NOW(), NOW(), 1, 1),
('Samsung Galaxy S24+ 256GB 雅岩灰', 599900, 450, 'https://example.com/images/s24plus.jpg', '手机', 'Samsung', '{"颜色":"雅岩灰","存储":"256GB","屏幕":"6.7英寸","芯片":"Exynos 2400"}', 86740, 15780, FALSE, 1, NOW(), NOW(), 1, 1),
('Samsung Galaxy S24 256GB 秘矿紫', 499900, 600, 'https://example.com/images/s24.jpg', '手机', 'Samsung', '{"颜色":"秘矿紫","存储":"256GB","屏幕":"6.2英寸","芯片":"Exynos 2400"}', 98720, 17890, FALSE, 1, NOW(), NOW(), 1, 1),
('Samsung Galaxy Z Fold6 256GB 幻影黑', 1399900, 180, 'https://example.com/images/zfold6.jpg', '手机', 'Samsung', '{"颜色":"幻影黑","存储":"256GB","屏幕":"7.6英寸折叠","芯片":"骁龙8 Gen3"}', 21340, 4256, TRUE, 1, NOW(), NOW(), 1, 1),
('Samsung Galaxy Z Fold6 512GB 冰晶蓝', 1549900, 120, 'https://example.com/images/zfold6.jpg', '手机', 'Samsung', '{"颜色":"冰晶蓝","存储":"512GB","屏幕":"7.6英寸折叠","芯片":"骁龙8 Gen3"}', 15098, 3120, FALSE, 1, NOW(), NOW(), 1, 1),
('Samsung Galaxy Z Fold6 1TB 幻影白', 1749900, 60, 'https://example.com/images/zfold6.jpg', '手机', 'Samsung', '{"颜色":"幻影白","存储":"1TB","屏幕":"7.6英寸折叠","芯片":"骁龙8 Gen3"}', 8720, 1567, FALSE, 1, NOW(), NOW(), 1, 1),
('Samsung Galaxy Z Flip6 256GB 薄荷绿', 799900, 350, 'https://example.com/images/zflip6.jpg', '手机', 'Samsung', '{"颜色":"薄荷绿","存储":"256GB","屏幕":"6.7英寸折叠","芯片":"骁龙8 Gen3"}', 54670, 10890, TRUE, 1, NOW(), NOW(), 1, 1),
('Samsung Galaxy Z Flip6 512GB 星夜银', 949900, 250, 'https://example.com/images/zflip6.jpg', '手机', 'Samsung', '{"颜色":"星夜银","存储":"512GB","屏幕":"6.7英寸折叠","芯片":"骁龙8 Gen3"}', 32450, 6520, FALSE, 1, NOW(), NOW(), 1, 1),
('Samsung Galaxy Z Fold 特别版 512GB', 1699900, 80, 'https://example.com/images/zfold-se.jpg', '手机', 'Samsung', '{"颜色":"幻影黑","存储":"512GB","屏幕":"8.0英寸折叠","芯片":"骁龙8 Gen3"}', 11230, 2130, FALSE, 1, NOW(), NOW(), 1, 1),
('Samsung Galaxy A55 256GB 幻夜黑', 249900, 900, 'https://example.com/images/a55.jpg', '手机', 'Samsung', '{"颜色":"幻夜黑","存储":"256GB","屏幕":"6.6英寸","芯片":"Exynos 1480"}', 67890, 12340, FALSE, 1, NOW(), NOW(), 1, 1),
('Samsung Galaxy A55 128GB 冰蓝', 199900, 1200, 'https://example.com/images/a55.jpg', '手机', 'Samsung', '{"颜色":"冰蓝","存储":"128GB","屏幕":"6.6英寸","芯片":"Exynos 1480"}', 89760, 16780, FALSE, 1, NOW(), NOW(), 1, 1),
('Samsung Galaxy A35 256GB 幻夜黑', 189900, 1100, 'https://example.com/images/a35.jpg', '手机', 'Samsung', '{"颜色":"幻夜黑","存储":"256GB","屏幕":"6.6英寸","芯片":"Exynos 1380"}', 54320, 10340, FALSE, 1, NOW(), NOW(), 1, 1),
('Samsung Galaxy A35 128GB 薰衣紫', 159900, 1400, 'https://example.com/images/a35.jpg', '手机', 'Samsung', '{"颜色":"薰衣紫","存储":"128GB","屏幕":"6.6英寸","芯片":"Exynos 1380"}', 78930, 14670, FALSE, 1, NOW(), NOW(), 1, 1),
('Samsung Galaxy M55 256GB 幻夜黑', 179900, 800, 'https://example.com/images/m55.jpg', '手机', 'Samsung', '{"颜色":"幻夜黑","存储":"256GB","屏幕":"6.7英寸","芯片":"骁龙7 Gen1"}', 34560, 6780, FALSE, 1, NOW(), NOW(), 1, 1),
('Samsung Galaxy S24 FE 256GB 蓝色', 419900, 550, 'https://example.com/images/s24fe.jpg', '手机', 'Samsung', '{"颜色":"蓝色","存储":"256GB","屏幕":"6.7英寸","芯片":"Exynos 2400e"}', 45670, 8760, FALSE, 1, NOW(), NOW(), 1, 1),
('Samsung Galaxy S25 Edge 256GB 钛灰', 899900, 150, 'https://example.com/images/s25edge.jpg', '手机', 'Samsung', '{"颜色":"钛灰色","存储":"256GB","屏幕":"6.7英寸","芯片":"骁龙8 Elite"}', 13200, 2650, TRUE, 1, NOW(), NOW(), 1, 1),
('Samsung Galaxy S25 Ultra 512GB 定制色 玉石绿', 1129900, 90, 'https://example.com/images/s25ultra.jpg', '手机', 'Samsung', '{"颜色":"玉石绿","存储":"512GB","屏幕":"6.9英寸","芯片":"骁龙8 Elite","备注":"定制色"}', 9870, 1980, FALSE, 1, NOW(), NOW(), 1, 1),
('Samsung Galaxy A16 256GB 黑色', 119900, 2000, 'https://example.com/images/a16.jpg', '手机', 'Samsung', '{"颜色":"黑色","存储":"256GB","屏幕":"6.7英寸","芯片":"联发科Helio G99"}', 102340, 20340, FALSE, 1, NOW(), NOW(), 1, 1),
('Samsung Galaxy A16 128GB 薄荷绿', 99900, 2500, 'https://example.com/images/a16.jpg', '手机', 'Samsung', '{"颜色":"薄荷绿","存储":"128GB","屏幕":"6.7英寸","芯片":"联发科Helio G99"}', 145670, 28760, FALSE, 1, NOW(), NOW(), 1, 1),
('Samsung Galaxy M15 128GB 暗夜黑', 89900, 1800, 'https://example.com/images/m15.jpg', '手机', 'Samsung', '{"颜色":"暗夜黑","存储":"128GB","屏幕":"6.5英寸","芯片":"联发科天玑6100+"}', 87650, 16780, FALSE, 1, NOW(), NOW(), 1, 1),
('Samsung W25 Flip 心系天下 512GB 陶瓷黑', 1299900, 50, 'https://example.com/images/w25flip.jpg', '手机', 'Samsung', '{"颜色":"陶瓷黑","存储":"512GB","屏幕":"6.7英寸折叠","芯片":"骁龙8 Gen3"}', 4560, 980, FALSE, 1, NOW(), NOW(), 1, 1),
('Samsung W25 心系天下 512GB 陶瓷黑', 1599900, 40, 'https://example.com/images/w25.jpg', '手机', 'Samsung', '{"颜色":"陶瓷黑","存储":"512GB","屏幕":"8.0英寸折叠","芯片":"骁龙8 Gen3"}', 3200, 670, FALSE, 1, NOW(), NOW(), 1, 1);

-- ======== 华为 Huawei (25条) ========
INSERT INTO item.item (name, price, stock, image, category, brand, spec, sold, comment_count, is_ad, status, create_time, update_time, creater, updater) VALUES
('华为 Mate 70 Pro+ 512GB 金丝银锦', 849900, 300, 'https://example.com/images/mate70proplus.jpg', '手机', '华为', '{"颜色":"金丝银锦","存储":"512GB","屏幕":"6.9英寸","芯片":"麒麟9100"}', 67890, 11240, TRUE, 1, NOW(), NOW(), 1, 1),
('华为 Mate 70 Pro+ 1TB 墨韵黑', 999900, 180, 'https://example.com/images/mate70proplus.jpg', '手机', '华为', '{"颜色":"墨韵黑","存储":"1TB","屏幕":"6.9英寸","芯片":"麒麟9100"}', 42310, 7890, FALSE, 1, NOW(), NOW(), 1, 1),
('华为 Mate 70 Pro 512GB 白沙银', 699900, 400, 'https://example.com/images/mate70pro.jpg', '手机', '华为', '{"颜色":"白沙银","存储":"512GB","屏幕":"6.9英寸","芯片":"麒麟9100"}', 58930, 9870, TRUE, 1, NOW(), NOW(), 1, 1),
('华为 Mate 70 Pro 256GB 雪域白', 629900, 550, 'https://example.com/images/mate70pro.jpg', '手机', '华为', '{"颜色":"雪域白","存储":"256GB","屏幕":"6.9英寸","芯片":"麒麟9100"}', 45670, 7650, FALSE, 1, NOW(), NOW(), 1, 1),
('华为 Mate 70 512GB 风信紫', 579900, 500, 'https://example.com/images/mate70.jpg', '手机', '华为', '{"颜色":"风信紫","存储":"512GB","屏幕":"6.7英寸","芯片":"麒麟9100"}', 38670, 6450, FALSE, 1, NOW(), NOW(), 1, 1),
('华为 Mate 70 RS 非凡大师 1TB 玄黑', 1299900, 100, 'https://example.com/images/mate70-rs.jpg', '手机', '华为', '{"颜色":"玄黑","存储":"1TB","屏幕":"6.9英寸","芯片":"麒麟9100","设计":"陶瓷机身"}', 15670, 3120, TRUE, 1, NOW(), NOW(), 1, 1),
('华为 Mate 70 RS 非凡大师 512GB 皓白', 1199900, 120, 'https://example.com/images/mate70-rs.jpg', '手机', '华为', '{"颜色":"皓白","存储":"512GB","屏幕":"6.9英寸","芯片":"麒麟9100","设计":"陶瓷机身"}', 11230, 2230, FALSE, 1, NOW(), NOW(), 1, 1),
('华为 Mate X6 折叠屏 512GB 星云灰', 1299900, 150, 'https://example.com/images/matex6.jpg', '手机', '华为', '{"颜色":"星云灰","存储":"512GB","屏幕":"7.93英寸折叠","芯片":"麒麟9100"}', 21340, 4230, TRUE, 1, NOW(), NOW(), 1, 1),
('华为 Mate X6 折叠屏 1TB 曜金黑', 1499900, 80, 'https://example.com/images/matex6.jpg', '手机', '华为', '{"颜色":"曜金黑","存储":"1TB","屏幕":"7.93英寸折叠","芯片":"麒麟9100"}', 12670, 2670, FALSE, 1, NOW(), NOW(), 1, 1),
('华为 Mate X6 典藏版 512GB 丹霞橙', 1599900, 50, 'https://example.com/images/matex6-collector.jpg', '手机', '华为', '{"颜色":"丹霞橙","存储":"512GB","屏幕":"7.93英寸折叠","芯片":"麒麟9100","备注":"典藏版"}', 6780, 1340, FALSE, 1, NOW(), NOW(), 1, 1),
('华为 Pura 70 Ultra 512GB 香颂绿', 999900, 200, 'https://example.com/images/pura70ultra.jpg', '手机', '华为', '{"颜色":"香颂绿","存储":"512GB","屏幕":"6.8英寸","芯片":"麒麟9010"}', 48760, 8760, TRUE, 1, NOW(), NOW(), 1, 1),
('华为 Pura 70 Ultra 1TB 摩卡棕', 1199900, 130, 'https://example.com/images/pura70ultra.jpg', '手机', '华为', '{"颜色":"摩卡棕","存储":"1TB","屏幕":"6.8英寸","芯片":"麒麟9010"}', 30450, 5670, FALSE, 1, NOW(), NOW(), 1, 1),
('华为 Pura 70 Pro+ 512GB 魅影黑', 799900, 280, 'https://example.com/images/pura70proplus.jpg', '手机', '华为', '{"颜色":"魅影黑","存储":"512GB","屏幕":"6.8英寸","芯片":"麒麟9010"}', 36540, 6540, FALSE, 1, NOW(), NOW(), 1, 1),
('华为 Pura 70 Pro 256GB 羽砂黑', 649900, 380, 'https://example.com/images/pura70pro.jpg', '手机', '华为', '{"颜色":"羽砂黑","存储":"256GB","屏幕":"6.8英寸","芯片":"麒麟9000S"}', 40980, 7200, FALSE, 1, NOW(), NOW(), 1, 1),
('华为 Pura 70 512GB 雪域白', 549900, 450, 'https://example.com/images/pura70.jpg', '手机', '华为', '{"颜色":"雪域白","存储":"512GB","屏幕":"6.6英寸","芯片":"麒麟9000S"}', 35460, 6120, FALSE, 1, NOW(), NOW(), 1, 1),
('华为 Pura 80 Ultra 512GB 星夜黑', 1099900, 120, 'https://example.com/images/pura80ultra.jpg', '手机', '华为', '{"颜色":"星夜黑","存储":"512GB","屏幕":"6.8英寸","芯片":"麒麟9100"}', 21300, 4350, TRUE, 1, NOW(), NOW(), 1, 1),
('华为 nova 13 Pro 512GB 洛登绿', 399900, 600, 'https://example.com/images/nova13pro.jpg', '手机', '华为', '{"颜色":"洛登绿","存储":"512GB","屏幕":"6.76英寸","芯片":"麒麟8000"}', 56780, 10230, FALSE, 1, NOW(), NOW(), 1, 1),
('华为 nova 13 Pro 256GB 羽砂白', 349900, 750, 'https://example.com/images/nova13pro.jpg', '手机', '华为', '{"颜色":"羽砂白","存储":"256GB","屏幕":"6.76英寸","芯片":"麒麟8000"}', 45670, 8760, FALSE, 1, NOW(), NOW(), 1, 1),
('华为 nova 13 256GB 星耀黑', 269900, 900, 'https://example.com/images/nova13.jpg', '手机', '华为', '{"颜色":"星耀黑","存储":"256GB","屏幕":"6.7英寸","芯片":"麒麟7000"}', 45630, 7650, FALSE, 1, NOW(), NOW(), 1, 1),
('华为 nova 12 活力版 256GB 樱语粉', 199900, 1000, 'https://example.com/images/nova12lite.jpg', '手机', '华为', '{"颜色":"樱语粉","存储":"256GB","屏幕":"6.7英寸","芯片":"骁龙778G"}', 87650, 15670, FALSE, 1, NOW(), NOW(), 1, 1),
('华为 畅享 70 Pro 256GB 幻夜黑', 169900, 1200, 'https://example.com/images/changxiang70pro.jpg', '手机', '华为', '{"颜色":"幻夜黑","存储":"256GB","屏幕":"6.75英寸","芯片":"骁龙680"}', 98760, 18970, FALSE, 1, NOW(), NOW(), 1, 1),
('华为 畅享 70 128GB 雪域白', 129900, 1500, 'https://example.com/images/changxiang70.jpg', '手机', '华为', '{"颜色":"雪域白","存储":"128GB","屏幕":"6.75英寸","芯片":"麒麟710A"}', 123450, 23450, FALSE, 1, NOW(), NOW(), 1, 1),
('华为 Mate 60 Pro+ 512GB 砚黑', 799900, 150, 'https://example.com/images/mate60proplus.jpg', '手机', '华为', '{"颜色":"砚黑","存储":"512GB","屏幕":"6.82英寸","芯片":"麒麟9000S"}', 106780, 19870, FALSE, 1, NOW(), NOW(), 1, 1),
('华为 Mate 60 Pro 256GB 雅丹黑', 649900, 200, 'https://example.com/images/mate60pro.jpg', '手机', '华为', '{"颜色":"雅丹黑","存储":"256GB","屏幕":"6.82英寸","芯片":"麒麟9000S"}', 145670, 27890, FALSE, 1, NOW(), NOW(), 1, 1),
('华为 P70 Pro 512GB 雪域白', 699900, 180, 'https://example.com/images/p70pro.jpg', '手机', '华为', '{"颜色":"雪域白","存储":"512GB","屏幕":"6.8英寸","芯片":"麒麟9000S"}', 42980, 7650, FALSE, 1, NOW(), NOW(), 1, 1);

-- ======== 小米 Xiaomi (25条) ========
INSERT INTO item.item (name, price, stock, image, category, brand, spec, sold, comment_count, is_ad, status, create_time, update_time, creater, updater) VALUES
('Xiaomi 15 Ultra 512GB 经典黑', 699900, 350, 'https://example.com/images/xiaomi15ultra.jpg', '手机', '小米', '{"颜色":"经典黑","存储":"512GB","屏幕":"6.73英寸","芯片":"骁龙8 Elite"}', 52340, 9870, TRUE, 1, NOW(), NOW(), 1, 1),
('Xiaomi 15 Ultra 1TB 白色', 799900, 200, 'https://example.com/images/xiaomi15ultra.jpg', '手机', '小米', '{"颜色":"白色","存储":"1TB","屏幕":"6.73英寸","芯片":"骁龙8 Elite"}', 32100, 6230, FALSE, 1, NOW(), NOW(), 1, 1),
('Xiaomi 15 Pro 512GB 岩石青', 549900, 450, 'https://example.com/images/xiaomi15pro.jpg', '手机', '小米', '{"颜色":"岩石青","存储":"512GB","屏幕":"6.73英寸","芯片":"骁龙8 Elite"}', 45670, 8320, FALSE, 1, NOW(), NOW(), 1, 1),
('Xiaomi 15 Pro 256GB 亮银版', 499900, 550, 'https://example.com/images/xiaomi15pro.jpg', '手机', '小米', '{"颜色":"亮银色","存储":"256GB","屏幕":"6.73英寸","芯片":"骁龙8 Elite"}', 38920, 7120, FALSE, 1, NOW(), NOW(), 1, 1),
('Xiaomi 15 512GB 黑色', 449900, 650, 'https://example.com/images/xiaomi15.jpg', '手机', '小米', '{"颜色":"黑色","存储":"512GB","屏幕":"6.36英寸","芯片":"骁龙8 Elite"}', 51450, 9650, TRUE, 1, NOW(), NOW(), 1, 1),
('Xiaomi 15 256GB 浅草绿', 399900, 800, 'https://example.com/images/xiaomi15.jpg', '手机', '小米', '{"颜色":"浅草绿","存储":"256GB","屏幕":"6.36英寸","芯片":"骁龙8 Elite"}', 60980, 11340, TRUE, 1, NOW(), NOW(), 1, 1),
('Xiaomi 14 Ultra 512GB 黑色', 599900, 280, 'https://example.com/images/xiaomi14ultra.jpg', '手机', '小米', '{"颜色":"黑色","存储":"512GB","屏幕":"6.73英寸","芯片":"骁龙8 Gen3"}', 78650, 14870, FALSE, 1, NOW(), NOW(), 1, 1),
('Xiaomi 14 Pro 512GB 岩石青', 469900, 380, 'https://example.com/images/xiaomi14pro.jpg', '手机', '小米', '{"颜色":"岩石青","存储":"512GB","屏幕":"6.73英寸","芯片":"骁龙8 Gen3"}', 72340, 13670, FALSE, 1, NOW(), NOW(), 1, 1),
('Xiaomi 14 256GB 白色', 349900, 600, 'https://example.com/images/xiaomi14.jpg', '手机', '小米', '{"颜色":"白色","存储":"256GB","屏幕":"6.36英寸","芯片":"骁龙8 Gen3"}', 98760, 18790, FALSE, 1, NOW(), NOW(), 1, 1),
('Xiaomi MIX Fold 4 512GB 黑色', 799900, 180, 'https://example.com/images/mixfold4.jpg', '手机', '小米', '{"颜色":"黑色","存储":"512GB","屏幕":"7.98英寸折叠","芯片":"骁龙8 Gen3"}', 18920, 3670, TRUE, 1, NOW(), NOW(), 1, 1),
('Xiaomi MIX Fold 4 1TB 龙鳞纤维版', 949900, 80, 'https://example.com/images/mixfold4.jpg', '手机', '小米', '{"颜色":"龙鳞纤维","存储":"1TB","屏幕":"7.98英寸折叠","芯片":"骁龙8 Gen3"}', 9870, 2030, FALSE, 1, NOW(), NOW(), 1, 1),
('Xiaomi MIX Flip 512GB 幻影紫', 599900, 250, 'https://example.com/images/mixflip.jpg', '手机', '小米', '{"颜色":"幻影紫","存储":"512GB","屏幕":"6.86英寸折叠","芯片":"骁龙8 Gen3"}', 24320, 4650, FALSE, 1, NOW(), NOW(), 1, 1),
('Xiaomi 15 Ultra 钛金属特别版 512GB', 899900, 100, 'https://example.com/images/xiaomi15ultra-ti.jpg', '手机', '小米', '{"颜色":"钛金属","存储":"512GB","屏幕":"6.73英寸","芯片":"骁龙8 Elite","材质":"钛合金"}', 14500, 2980, TRUE, 1, NOW(), NOW(), 1, 1),
('Redmi K80 Pro 512GB 晴雪白', 389900, 500, 'https://example.com/images/redmik80pro.jpg', '手机', '红米', '{"颜色":"晴雪白","存储":"512GB","屏幕":"6.67英寸","芯片":"骁龙8 Gen3"}', 67890, 12340, TRUE, 1, NOW(), NOW(), 1, 1),
('Redmi K80 Pro 256GB 墨羽黑', 339900, 650, 'https://example.com/images/redmik80pro.jpg', '手机', '红米', '{"颜色":"墨羽黑","存储":"256GB","屏幕":"6.67英寸","芯片":"骁龙8 Gen3"}', 54320, 9870, FALSE, 1, NOW(), NOW(), 1, 1),
('Redmi K80 512GB 影青', 289900, 700, 'https://example.com/images/redmik80.jpg', '手机', '红米', '{"颜色":"影青","存储":"512GB","屏幕":"6.67英寸","芯片":"骁龙8 Gen2"}', 56430, 10560, FALSE, 1, NOW(), NOW(), 1, 1),
('Redmi K80 256GB 幻夜黑', 249900, 900, 'https://example.com/images/redmik80.jpg', '手机', '红米', '{"颜色":"幻夜黑","存储":"256GB","屏幕":"6.67英寸","芯片":"骁龙8 Gen2"}', 78650, 14670, FALSE, 1, NOW(), NOW(), 1, 1),
('Redmi Note 14 Pro+ 512GB 镜瓷白', 219900, 800, 'https://example.com/images/redminote14proplus.jpg', '手机', '红米', '{"颜色":"镜瓷白","存储":"512GB","屏幕":"6.67英寸","芯片":"骁龙7s Gen3"}', 98760, 18790, FALSE, 1, NOW(), NOW(), 1, 1),
('Redmi Note 14 Pro+ 256GB 子夜黑', 189900, 1000, 'https://example.com/images/redminote14proplus.jpg', '手机', '红米', '{"颜色":"子夜黑","存储":"256GB","屏幕":"6.67英寸","芯片":"骁龙7s Gen3"}', 112340, 21340, FALSE, 1, NOW(), NOW(), 1, 1),
('Redmi Note 14 Pro 256GB 幻影蓝', 159900, 1200, 'https://example.com/images/redminote14pro.jpg', '手机', '红米', '{"颜色":"幻影蓝","存储":"256GB","屏幕":"6.67英寸","芯片":"天玑7300"}', 134560, 25670, FALSE, 1, NOW(), NOW(), 1, 1),
('Redmi Note 14 128GB 冰晶绿', 109900, 1800, 'https://example.com/images/redminote14.jpg', '手机', '红米', '{"颜色":"冰晶绿","存储":"128GB","屏幕":"6.67英寸","芯片":"天玑6100+"}', 187650, 34560, FALSE, 1, NOW(), NOW(), 1, 1),
('Redmi 14C 128GB 午夜黑', 79900, 2000, 'https://example.com/images/redmi14c.jpg', '手机', '红米', '{"颜色":"午夜黑","存储":"128GB","屏幕":"6.68英寸","芯片":"联发科Helio G81"}', 213450, 40320, FALSE, 1, NOW(), NOW(), 1, 1),
('Redmi Turbo 4 Pro 512GB 暗影黑', 269900, 550, 'https://example.com/images/redmiturbo4pro.jpg', '手机', '红米', '{"颜色":"暗影黑","存储":"512GB","屏幕":"6.67英寸","芯片":"骁龙8s Gen3"}', 34560, 6780, FALSE, 1, NOW(), NOW(), 1, 1),
('Xiaomi Civi 5 Pro 512GB 春野绿', 349900, 400, 'https://example.com/images/xiaomicivi5pro.jpg', '手机', '小米', '{"颜色":"春野绿","存储":"512GB","屏幕":"6.55英寸","芯片":"骁龙8s Gen3"}', 28760, 5340, FALSE, 1, NOW(), NOW(), 1, 1),
('Xiaomi 14T Pro 512GB 钛原色', 499900, 300, 'https://example.com/images/xiaomi14tpro.jpg', '手机', '小米', '{"颜色":"钛原色","存储":"512GB","屏幕":"6.67英寸","芯片":"天玑9300+"}', 26780, 4870, FALSE, 1, NOW(), NOW(), 1, 1);

-- ======== OPPO (20条) ========
INSERT INTO item.item (name, price, stock, image, category, brand, spec, sold, comment_count, is_ad, status, create_time, update_time, creater, updater) VALUES
('OPPO Find X8 Pro 512GB 星野黑', 599900, 350, 'https://example.com/images/findx8pro.jpg', '手机', 'OPPO', '{"颜色":"星野黑","存储":"512GB","屏幕":"6.78英寸","芯片":"天玑9400"}', 42310, 7890, TRUE, 1, NOW(), NOW(), 1, 1),
('OPPO Find X8 Pro 256GB 漫步云端', 549900, 450, 'https://example.com/images/findx8pro.jpg', '手机', 'OPPO', '{"颜色":"漫步云端","存储":"256GB","屏幕":"6.78英寸","芯片":"天玑9400"}', 34560, 6540, FALSE, 1, NOW(), NOW(), 1, 1),
('OPPO Find X8 512GB 星芒白', 449900, 500, 'https://example.com/images/findx8.jpg', '手机', 'OPPO', '{"颜色":"星芒白","存储":"512GB","屏幕":"6.59英寸","芯片":"天玑9400"}', 38920, 7230, FALSE, 1, NOW(), NOW(), 1, 1),
('OPPO Find X8 256GB 追风蓝', 399900, 600, 'https://example.com/images/findx8.jpg', '手机', 'OPPO', '{"颜色":"追风蓝","存储":"256GB","屏幕":"6.59英寸","芯片":"天玑9400"}', 45670, 8760, FALSE, 1, NOW(), NOW(), 1, 1),
('OPPO Find X7 Ultra 512GB 海阔天空', 549900, 280, 'https://example.com/images/findx7ultra.jpg', '手机', 'OPPO', '{"颜色":"海阔天空","存储":"512GB","屏幕":"6.82英寸","芯片":"骁龙8 Gen3"}', 56780, 10670, FALSE, 1, NOW(), NOW(), 1, 1),
('OPPO Find N5 折叠屏 512GB 星夜黑', 899900, 150, 'https://example.com/images/findn5.jpg', '手机', 'OPPO', '{"颜色":"星夜黑","存储":"512GB","屏幕":"8.0英寸折叠","芯片":"骁龙8 Elite"}', 16780, 3450, TRUE, 1, NOW(), NOW(), 1, 1),
('OPPO Find N5 折叠屏 1TB 晨曦白', 1049900, 80, 'https://example.com/images/findn5.jpg', '手机', 'OPPO', '{"颜色":"晨曦白","存储":"1TB","屏幕":"8.0英寸折叠","芯片":"骁龙8 Elite"}', 9870, 1980, FALSE, 1, NOW(), NOW(), 1, 1),
('OPPO Reno 13 Pro 512GB 蝶蝶紫', 349900, 500, 'https://example.com/images/reno13pro.jpg', '手机', 'OPPO', '{"颜色":"蝶蝶紫","存储":"512GB","屏幕":"6.83英寸","芯片":"天玑8350"}', 45670, 8320, FALSE, 1, NOW(), NOW(), 1, 1),
('OPPO Reno 13 Pro 256GB 星河银', 299900, 650, 'https://example.com/images/reno13pro.jpg', '手机', 'OPPO', '{"颜色":"星河银","存储":"256GB","屏幕":"6.83英寸","芯片":"天玑8350"}', 38920, 7120, FALSE, 1, NOW(), NOW(), 1, 1),
('OPPO Reno 13 256GB 暗夜黑', 249900, 800, 'https://example.com/images/reno13.jpg', '手机', 'OPPO', '{"颜色":"暗夜黑","存储":"256GB","屏幕":"6.59英寸","芯片":"天玑7300"}', 60980, 11230, FALSE, 1, NOW(), NOW(), 1, 1),
('OPPO Reno 12 Pro 256GB 银幻紫', 219900, 700, 'https://example.com/images/reno12pro.jpg', '手机', 'OPPO', '{"颜色":"银幻紫","存储":"256GB","屏幕":"6.7英寸","芯片":"天玑7300"}', 76540, 14320, FALSE, 1, NOW(), NOW(), 1, 1),
('OPPO A5 Pro 256GB 星夜黑', 179900, 900, 'https://example.com/images/a5pro.jpg', '手机', 'OPPO', '{"颜色":"星夜黑","存储":"256GB","屏幕":"6.7英寸","芯片":"骁龙6 Gen1"}', 98760, 18790, FALSE, 1, NOW(), NOW(), 1, 1),
('OPPO A5 128GB 海蓝', 109900, 1500, 'https://example.com/images/oppoa5.jpg', '手机', 'OPPO', '{"颜色":"海蓝","存储":"128GB","屏幕":"6.56英寸","芯片":"天玑6020"}', 156780, 29870, FALSE, 1, NOW(), NOW(), 1, 1),
('OPPO K13 256GB 星夜黑', 149900, 1000, 'https://example.com/images/oppok13.jpg', '手机', 'OPPO', '{"颜色":"星夜黑","存储":"256GB","屏幕":"6.67英寸","芯片":"天玑6300"}', 87650, 16540, FALSE, 1, NOW(), NOW(), 1, 1),
('OPPO Find N5 Flip 512GB 花漾粉', 699900, 200, 'https://example.com/images/findn5flip.jpg', '手机', 'OPPO', '{"颜色":"花漾粉","存储":"512GB","屏幕":"6.8英寸折叠","芯片":"天玑9400"}', 19870, 3890, TRUE, 1, NOW(), NOW(), 1, 1),
('OPPO Find X8 Ultra 512GB 深海蓝', 699900, 200, 'https://example.com/images/findx8ultra.jpg', '手机', 'OPPO', '{"颜色":"深海蓝","存储":"512GB","屏幕":"6.82英寸","芯片":"骁龙8 Elite"}', 26780, 5340, FALSE, 1, NOW(), NOW(), 1, 1),
('OPPO Find X9 256GB 幻夜黑', 479900, 300, 'https://example.com/images/findx9.jpg', '手机', 'OPPO', '{"颜色":"幻夜黑","存储":"256GB","屏幕":"6.78英寸","芯片":"天玑9500"}', 21340, 4230, FALSE, 1, NOW(), NOW(), 1, 1),
('OPPO Reno 14 青春版 256GB 星云紫', 149900, 700, 'https://example.com/images/reno14-lite.jpg', '手机', 'OPPO', '{"颜色":"星云紫","存储":"256GB","屏幕":"6.59英寸","芯片":"天玑7200"}', 56780, 10230, FALSE, 1, NOW(), NOW(), 1, 1),
('OPPO A3 Pro 256GB 远山蓝', 129900, 1200, 'https://example.com/images/oppoa3pro.jpg', '手机', 'OPPO', '{"颜色":"远山蓝","存储":"256GB","屏幕":"6.7英寸","芯片":"骁龙695"}', 123450, 23450, FALSE, 1, NOW(), NOW(), 1, 1),
('OPPO K12 5G 256GB 星夜黑', 139900, 800, 'https://example.com/images/oppok12.jpg', '手机', 'OPPO', '{"颜色":"星夜黑","存储":"256GB","屏幕":"6.67英寸","芯片":"骁龙7 Gen3"}', 76540, 14320, FALSE, 1, NOW(), NOW(), 1, 1);

-- ======== vivo (20条) ========
INSERT INTO item.item (name, price, stock, image, category, brand, spec, sold, comment_count, is_ad, status, create_time, update_time, creater, updater) VALUES
('vivo X200 Pro 512GB 午夜黑', 599900, 320, 'https://example.com/images/vivox200pro.jpg', '手机', 'vivo', '{"颜色":"午夜黑","存储":"512GB","屏幕":"6.78英寸","芯片":"天玑9400"}', 38920, 7230, TRUE, 1, NOW(), NOW(), 1, 1),
('vivo X200 Pro 256GB 白月光', 549900, 420, 'https://example.com/images/vivox200pro.jpg', '手机', 'vivo', '{"颜色":"白月光","存储":"256GB","屏幕":"6.78英寸","芯片":"天玑9400"}', 30450, 5780, FALSE, 1, NOW(), NOW(), 1, 1),
('vivo X200 512GB 钛青', 449900, 500, 'https://example.com/images/vivox200.jpg', '手机', 'vivo', '{"颜色":"钛青","存储":"512GB","屏幕":"6.67英寸","芯片":"天玑9400"}', 35670, 6540, FALSE, 1, NOW(), NOW(), 1, 1),
('vivo X200 256GB 辰夜黑', 399900, 600, 'https://example.com/images/vivox200.jpg', '手机', 'vivo', '{"颜色":"辰夜黑","存储":"256GB","屏幕":"6.67英寸","芯片":"天玑9400"}', 45670, 8760, FALSE, 1, NOW(), NOW(), 1, 1),
('vivo X100 Ultra 512GB 钛色', 649900, 250, 'https://example.com/images/vivox100ultra.jpg', '手机', 'vivo', '{"颜色":"钛色","存储":"512GB","屏幕":"6.78英寸","芯片":"骁龙8 Gen3"}', 52340, 9870, TRUE, 1, NOW(), NOW(), 1, 1),
('vivo X100s Pro 512GB 白月光', 499900, 350, 'https://example.com/images/vivox100spro.jpg', '手机', 'vivo', '{"颜色":"白月光","存储":"512GB","屏幕":"6.78英寸","芯片":"天玑9300+"}', 44320, 8120, FALSE, 1, NOW(), NOW(), 1, 1),
('vivo X100s 256GB 青云', 429900, 450, 'https://example.com/images/vivox100s.jpg', '手机', 'vivo', '{"颜色":"青云","存储":"256GB","屏幕":"6.78英寸","芯片":"天玑9300+"}', 36780, 6890, FALSE, 1, NOW(), NOW(), 1, 1),
('vivo X Fold 4 Pro 512GB 星夜黑', 849900, 120, 'https://example.com/images/vivoxfold4pro.jpg', '手机', 'vivo', '{"颜色":"星夜黑","存储":"512GB","屏幕":"8.03英寸折叠","芯片":"骁龙8 Elite"}', 15670, 3120, TRUE, 1, NOW(), NOW(), 1, 1),
('vivo X Fold 4 Pro 1TB 碧海蓝', 999900, 60, 'https://example.com/images/vivoxfold4pro.jpg', '手机', 'vivo', '{"颜色":"碧海蓝","存储":"1TB","屏幕":"8.03英寸折叠","芯片":"骁龙8 Elite"}', 9870, 1980, FALSE, 1, NOW(), NOW(), 1, 1),
('vivo S20 Pro 512GB 花似锦', 329900, 450, 'https://example.com/images/vivos20pro.jpg', '手机', 'vivo', '{"颜色":"花似锦","存储":"512GB","屏幕":"6.78英寸","芯片":"天玑8200"}', 34560, 6540, FALSE, 1, NOW(), NOW(), 1, 1),
('vivo S20 256GB 松烟墨', 269900, 600, 'https://example.com/images/vivos20.jpg', '手机', 'vivo', '{"颜色":"松烟墨","存储":"256GB","屏幕":"6.67英寸","芯片":"骁龙7 Gen3"}', 48930, 8760, FALSE, 1, NOW(), NOW(), 1, 1),
('vivo S19 Pro 256GB 烟雨青', 299900, 500, 'https://example.com/images/vivos19pro.jpg', '手机', 'vivo', '{"颜色":"烟雨青","存储":"256GB","屏幕":"6.78英寸","芯片":"天玑9200+"}', 36780, 6780, FALSE, 1, NOW(), NOW(), 1, 1),
('vivo Y300 Pro 256GB 星夜黑', 179900, 900, 'https://example.com/images/vivoy300pro.jpg', '手机', 'vivo', '{"颜色":"星夜黑","存储":"256GB","屏幕":"6.77英寸","芯片":"骁龙6 Gen1"}', 87650, 16780, FALSE, 1, NOW(), NOW(), 1, 1),
('vivo Y300 128GB 湖光绿', 149900, 1100, 'https://example.com/images/vivoy300.jpg', '手机', 'vivo', '{"颜色":"湖光绿","存储":"128GB","屏幕":"6.64英寸","芯片":"天玑6020"}', 102340, 19870, FALSE, 1, NOW(), NOW(), 1, 1),
('vivo Y200 128GB 石青', 119900, 1500, 'https://example.com/images/vivoy200.jpg', '手机', 'vivo', '{"颜色":"石青","存储":"128GB","屏幕":"6.64英寸","芯片":"天玑6100+"}', 145670, 26780, FALSE, 1, NOW(), NOW(), 1, 1),
('vivo X200 Ultra 512GB 深海蓝', 749900, 180, 'https://example.com/images/vivox200ultra.jpg', '手机', 'vivo', '{"颜色":"深海蓝","存储":"512GB","屏幕":"6.78英寸","芯片":"骁龙8 Elite"}', 23450, 4560, FALSE, 1, NOW(), NOW(), 1, 1),
('vivo X200 Pro mini 256GB 微粉', 429900, 300, 'https://example.com/images/vivox200promini.jpg', '手机', 'vivo', '{"颜色":"微粉","存储":"256GB","屏幕":"6.31英寸","芯片":"天玑9400"}', 28760, 5340, FALSE, 1, NOW(), NOW(), 1, 1),
('vivo T4 5G 256GB 暗影黑', 199900, 700, 'https://example.com/images/vivot4.jpg', '手机', 'vivo', '{"颜色":"暗影黑","存储":"256GB","屏幕":"6.67英寸","芯片":"骁龙7 Gen3"}', 45670, 8760, FALSE, 1, NOW(), NOW(), 1, 1),
('vivo iQOO 13 512GB 赛道版', 449900, 400, 'https://example.com/images/iqoo13.jpg', '手机', 'iQOO', '{"颜色":"赛道版","存储":"512GB","屏幕":"6.82英寸","芯片":"骁龙8 Elite"}', 42310, 7890, TRUE, 1, NOW(), NOW(), 1, 1),
('vivo iQOO Neo10 Pro 256GB 疾影黑', 299900, 500, 'https://example.com/images/iqooneo10pro.jpg', '手机', 'iQOO', '{"颜色":"疾影黑","存储":"256GB","屏幕":"6.78英寸","芯片":"天玑9300+"}', 34560, 6540, FALSE, 1, NOW(), NOW(), 1, 1);

-- ======== 荣耀 Honor (15条) ========
INSERT INTO item.item (name, price, stock, image, category, brand, spec, sold, comment_count, is_ad, status, create_time, update_time, creater, updater) VALUES
('荣耀 Magic7 Pro 512GB 月影灰', 599900, 300, 'https://example.com/images/magic7pro.jpg', '手机', '荣耀', '{"颜色":"月影灰","存储":"512GB","屏幕":"6.8英寸","芯片":"骁龙8 Elite"}', 38920, 7340, TRUE, 1, NOW(), NOW(), 1, 1),
('荣耀 Magic7 Pro 1TB 天际蓝', 699900, 180, 'https://example.com/images/magic7pro.jpg', '手机', '荣耀', '{"颜色":"天际蓝","存储":"1TB","屏幕":"6.8英寸","芯片":"骁龙8 Elite"}', 23450, 4560, FALSE, 1, NOW(), NOW(), 1, 1),
('荣耀 Magic7 512GB 朝霞金', 499900, 400, 'https://example.com/images/magic7.jpg', '手机', '荣耀', '{"颜色":"朝霞金","存储":"512GB","屏幕":"6.78英寸","芯片":"骁龙8 Elite"}', 34560, 6450, FALSE, 1, NOW(), NOW(), 1, 1),
('荣耀 Magic7 256GB 亮黑色', 449900, 500, 'https://example.com/images/magic7.jpg', '手机', '荣耀', '{"颜色":"亮黑色","存储":"256GB","屏幕":"6.78英寸","芯片":"骁龙8 Elite"}', 40980, 7890, FALSE, 1, NOW(), NOW(), 1, 1),
('荣耀 Magic V4 折叠屏 512GB 雅黑', 799900, 150, 'https://example.com/images/magicv4.jpg', '手机', '荣耀', '{"颜色":"雅黑","存储":"512GB","屏幕":"7.92英寸折叠","芯片":"骁龙8 Elite"}', 18760, 3450, TRUE, 1, NOW(), NOW(), 1, 1),
('荣耀 Magic V4 折叠屏 1TB 金丝', 949900, 70, 'https://example.com/images/magicv4.jpg', '手机', '荣耀', '{"颜色":"金丝","存储":"1TB","屏幕":"7.92英寸折叠","芯片":"骁龙8 Elite"}', 9870, 1890, FALSE, 1, NOW(), NOW(), 1, 1),
('荣耀 Magic6 Pro 512GB 祁连雪', 499900, 250, 'https://example.com/images/magic6pro.jpg', '手机', '荣耀', '{"颜色":"祁连雪","存储":"512GB","屏幕":"6.8英寸","芯片":"骁龙8 Gen3"}', 45670, 8760, FALSE, 1, NOW(), NOW(), 1, 1),
('荣耀 300 Pro 512GB 墨岩黑', 349900, 450, 'https://example.com/images/honor300pro.jpg', '手机', '荣耀', '{"颜色":"墨岩黑","存储":"512GB","屏幕":"6.78英寸","芯片":"骁龙8 Gen3"}', 38920, 7120, FALSE, 1, NOW(), NOW(), 1, 1),
('荣耀 300 256GB 星光蓝', 269900, 650, 'https://example.com/images/honor300.jpg', '手机', '荣耀', '{"颜色":"星光蓝","存储":"256GB","屏幕":"6.7英寸","芯片":"骁龙7 Gen3"}', 45670, 8450, FALSE, 1, NOW(), NOW(), 1, 1),
('荣耀 200 Pro 512GB 天海青', 299900, 500, 'https://example.com/images/honor200pro.jpg', '手机', '荣耀', '{"颜色":"天海青","存储":"512GB","屏幕":"6.78英寸","芯片":"骁龙8s Gen3"}', 36780, 6780, FALSE, 1, NOW(), NOW(), 1, 1),
('荣耀 X60 Pro 256GB 幻夜黑', 179900, 800, 'https://example.com/images/honorx60pro.jpg', '手机', '荣耀', '{"颜色":"幻夜黑","存储":"256GB","屏幕":"6.78英寸","芯片":"骁龙6 Gen1"}', 98760, 18790, FALSE, 1, NOW(), NOW(), 1, 1),
('荣耀 X60 256GB 幻夜黑', 149900, 1000, 'https://example.com/images/honorx60.jpg', '手机', '荣耀', '{"颜色":"幻夜黑","存储":"256GB","屏幕":"6.8英寸","芯片":"天玑6080"}', 112340, 21340, FALSE, 1, NOW(), NOW(), 1, 1),
('荣耀 X50 Pro 256GB 典雅黑', 169900, 900, 'https://example.com/images/honorx50pro.jpg', '手机', '荣耀', '{"颜色":"典雅黑","存储":"256GB","屏幕":"6.78英寸","芯片":"骁龙6 Gen1"}', 87650, 16780, FALSE, 1, NOW(), NOW(), 1, 1),
('荣耀 Magic7 RSR 保时捷设计 1TB', 1199900, 50, 'https://example.com/images/magic7-rsr.jpg', '手机', '荣耀', '{"颜色":"保时捷设计","存储":"1TB","屏幕":"6.8英寸","芯片":"骁龙8 Elite"}', 5430, 1230, FALSE, 1, NOW(), NOW(), 1, 1),
('荣耀 GT 256GB 幻影黑', 239900, 550, 'https://example.com/images/honorgt.jpg', '手机', '荣耀', '{"颜色":"幻影黑","存储":"256GB","屏幕":"6.78英寸","芯片":"骁龙8 Gen2"}', 32340, 6120, FALSE, 1, NOW(), NOW(), 1, 1);

-- ======== OnePlus 一加 / realme / 其他 (20条) ========
INSERT INTO item.item (name, price, stock, image, category, brand, spec, sold, comment_count, is_ad, status, create_time, update_time, creater, updater) VALUES
('一加 13 512GB 苍绿', 479900, 350, 'https://example.com/images/oneplus13.jpg', '手机', '一加', '{"颜色":"苍绿","存储":"512GB","屏幕":"6.82英寸","芯片":"骁龙8 Elite"}', 38920, 7230, TRUE, 1, NOW(), NOW(), 1, 1),
('一加 13 256GB 黑曜秘境', 429900, 450, 'https://example.com/images/oneplus13.jpg', '手机', '一加', '{"颜色":"黑曜秘境","存储":"256GB","屏幕":"6.82英寸","芯片":"骁龙8 Elite"}', 30450, 5780, FALSE, 1, NOW(), NOW(), 1, 1),
('一加 Ace 5 Pro 512GB 潜航黑', 349900, 500, 'https://example.com/images/oneplusace5pro.jpg', '手机', '一加', '{"颜色":"潜航黑","存储":"512GB","屏幕":"6.78英寸","芯片":"骁龙8 Gen3"}', 45670, 8320, FALSE, 1, NOW(), NOW(), 1, 1),
('一加 Ace 5 256GB 引力钛', 279900, 600, 'https://example.com/images/oneplusace5.jpg', '手机', '一加', '{"颜色":"引力钛","存储":"256GB","屏幕":"6.78英寸","芯片":"骁龙8 Gen2"}', 38760, 7120, FALSE, 1, NOW(), NOW(), 1, 1),
('一加 12 512GB 留白', 399900, 300, 'https://example.com/images/oneplus12.jpg', '手机', '一加', '{"颜色":"留白","存储":"512GB","屏幕":"6.82英寸","芯片":"骁龙8 Gen3"}', 56780, 10560, FALSE, 1, NOW(), NOW(), 1, 1),
('一加 Ace 3 Pro 512GB 钛空镜银', 329900, 400, 'https://example.com/images/oneplusace3pro.jpg', '手机', '一加', '{"颜色":"钛空镜银","存储":"512GB","屏幕":"6.78英寸","芯片":"骁龙8 Gen3"}', 32450, 6120, FALSE, 1, NOW(), NOW(), 1, 1),
('一加 Nord 4 256GB 乌木黑', 249900, 500, 'https://example.com/images/oneplusnord4.jpg', '手机', '一加', '{"颜色":"乌木黑","存储":"256GB","屏幕":"6.74英寸","芯片":"骁龙7+ Gen3"}', 28760, 5340, FALSE, 1, NOW(), NOW(), 1, 1),
('realme GT7 Pro 512GB 星际黑', 379900, 400, 'https://example.com/images/realmegt7pro.jpg', '手机', 'realme', '{"颜色":"星际黑","存储":"512GB","屏幕":"6.78英寸","芯片":"骁龙8 Elite"}', 34560, 6540, FALSE, 1, NOW(), NOW(), 1, 1),
('realme GT7 Pro 256GB 月光白', 329900, 500, 'https://example.com/images/realmegt7pro.jpg', '手机', 'realme', '{"颜色":"月光白","存储":"256GB","屏幕":"6.78英寸","芯片":"骁龙8 Elite"}', 27650, 5230, FALSE, 1, NOW(), NOW(), 1, 1),
('realme GT Neo6 512GB 钛空灰', 249900, 550, 'https://example.com/images/realmegtneo6.jpg', '手机', 'realme', '{"颜色":"钛空灰","存储":"512GB","屏幕":"6.78英寸","芯片":"骁龙8 Gen2"}', 38760, 7230, FALSE, 1, NOW(), NOW(), 1, 1),
('realme 14 Pro+ 256GB 海盐蓝', 199900, 650, 'https://example.com/images/realme14proplus.jpg', '手机', 'realme', '{"颜色":"海盐蓝","存储":"256GB","屏幕":"6.83英寸","芯片":"骁龙7s Gen3"}', 45670, 8760, FALSE, 1, NOW(), NOW(), 1, 1),
('realme 14 Pro 256GB 星夜黑', 169900, 800, 'https://example.com/images/realme14pro.jpg', '手机', 'realme', '{"颜色":"星夜黑","存储":"256GB","屏幕":"6.67英寸","芯片":"天玑7300"}', 56780, 10230, FALSE, 1, NOW(), NOW(), 1, 1),
('realme C75 128GB 暗夜黑', 89900, 1500, 'https://example.com/images/realmeC75.jpg', '手机', 'realme', '{"颜色":"暗夜黑","存储":"128GB","屏幕":"6.72英寸","芯片":"联发科Helio G92"}', 123450, 23140, FALSE, 1, NOW(), NOW(), 1, 1),
('中兴 Axon 60 Ultra 512GB 墨羽', 499900, 150, 'https://example.com/images/axon60ultra.jpg', '手机', '中兴', '{"颜色":"墨羽","存储":"512GB","屏幕":"6.8英寸","芯片":"骁龙8 Gen3"}', 12340, 2450, FALSE, 1, NOW(), NOW(), 1, 1),
('努比亚 Z70 Ultra 512GB 黑曜', 459900, 200, 'https://example.com/images/nubiaz70ultra.jpg', '手机', '努比亚', '{"颜色":"黑曜","存储":"512GB","屏幕":"6.85英寸","芯片":"骁龙8 Elite"}', 15670, 3120, FALSE, 1, NOW(), NOW(), 1, 1),
('努比亚 Red Magic 10 Pro 512GB 暗夜骑士', 549900, 180, 'https://example.com/images/redmagic10pro.jpg', '手机', '努比亚', '{"颜色":"暗夜骑士","存储":"512GB","屏幕":"6.85英寸","芯片":"骁龙8 Elite"}', 21340, 4230, TRUE, 1, NOW(), NOW(), 1, 1),
('努比亚 Red Magic 10 Pro+ 1TB 氘锋透明', 649900, 100, 'https://example.com/images/redmagic10proplus.jpg', '手机', '努比亚', '{"颜色":"氘锋透明","存储":"1TB","屏幕":"6.85英寸","芯片":"骁龙8 Elite"}', 12340, 2560, FALSE, 1, NOW(), NOW(), 1, 1),
('ROG Phone 9 Pro 512GB 幻影黑', 799900, 80, 'https://example.com/images/rogphone9pro.jpg', '手机', 'ROG', '{"颜色":"幻影黑","存储":"512GB","屏幕":"6.78英寸","芯片":"骁龙8 Elite"}', 8790, 1890, FALSE, 1, NOW(), NOW(), 1, 1),
('索尼 Xperia 1 VI 512GB 墨黑', 899900, 60, 'https://example.com/images/xperia1vi.jpg', '手机', '索尼', '{"颜色":"墨黑","存储":"512GB","屏幕":"6.5英寸4K","芯片":"骁龙8 Gen3"}', 7890, 1560, FALSE, 1, NOW(), NOW(), 1, 1),
('摩托罗拉 razr 60 Ultra 512GB 星云蓝', 599900, 120, 'https://example.com/images/razr60ultra.jpg', '手机', '摩托罗拉', '{"颜色":"星云蓝","存储":"512GB","屏幕":"6.9英寸折叠","芯片":"骁龙8 Elite"}', 12340, 2670, FALSE, 1, NOW(), NOW(), 1, 1);

-- 合计约205条数据 (手机~155 + 平板30 + 电脑20)

-- ======== Apple iPad (10条) ========
INSERT INTO item.item (name, price, stock, image, category, brand, spec, sold, comment_count, is_ad, status, create_time, update_time, creater, updater) VALUES
('iPad Pro 13 M4 256GB 深空黑色', 899900, 300, 'https://example.com/images/ipadpro13m4.jpg', '平板', 'Apple', '{"颜色":"深空黑色","存储":"256GB","屏幕":"13英寸","芯片":"M4"}', 45670, 8320, TRUE, 1, NOW(), NOW(), 1, 1),
('iPad Pro 13 M4 512GB 银色', 1049900, 200, 'https://example.com/images/ipadpro13m4.jpg', '平板', 'Apple', '{"颜色":"银色","存储":"512GB","屏幕":"13英寸","芯片":"M4"}', 32140, 5890, FALSE, 1, NOW(), NOW(), 1, 1),
('iPad Pro 11 M4 256GB 深空黑色', 699900, 400, 'https://example.com/images/ipadpro11m4.jpg', '平板', 'Apple', '{"颜色":"深空黑色","存储":"256GB","屏幕":"11英寸","芯片":"M4"}', 54320, 9760, TRUE, 1, NOW(), NOW(), 1, 1),
('iPad Pro 11 M4 512GB 银色', 849900, 250, 'https://example.com/images/ipadpro11m4.jpg', '平板', 'Apple', '{"颜色":"银色","存储":"512GB","屏幕":"11英寸","芯片":"M4"}', 38650, 6980, FALSE, 1, NOW(), NOW(), 1, 1),
('iPad Air M2 11英寸 128GB 星光色', 479900, 500, 'https://example.com/images/ipadairm2-11.jpg', '平板', 'Apple', '{"颜色":"星光色","存储":"128GB","屏幕":"11英寸","芯片":"M2"}', 67890, 12340, FALSE, 1, NOW(), NOW(), 1, 1),
('iPad Air M2 11英寸 256GB 蓝色', 569900, 350, 'https://example.com/images/ipadairm2-11.jpg', '平板', 'Apple', '{"颜色":"蓝色","存储":"256GB","屏幕":"11英寸","芯片":"M2"}', 48760, 8760, FALSE, 1, NOW(), NOW(), 1, 1),
('iPad Air M2 13英寸 256GB 深空灰', 659900, 280, 'https://example.com/images/ipadairm2-13.jpg', '平板', 'Apple', '{"颜色":"深空灰","存储":"256GB","屏幕":"13英寸","芯片":"M2"}', 34560, 6450, FALSE, 1, NOW(), NOW(), 1, 1),
('iPad mini 7 128GB 粉色', 399900, 450, 'https://example.com/images/ipadmini7.jpg', '平板', 'Apple', '{"颜色":"粉色","存储":"128GB","屏幕":"8.3英寸","芯片":"A17 Pro"}', 56780, 10230, FALSE, 1, NOW(), NOW(), 1, 1),
('iPad mini 7 256GB 紫色', 489900, 300, 'https://example.com/images/ipadmini7.jpg', '平板', 'Apple', '{"颜色":"紫色","存储":"256GB","屏幕":"8.3英寸","芯片":"A17 Pro"}', 38760, 7120, FALSE, 1, NOW(), NOW(), 1, 1),
('iPad 第11代 256GB 银色', 349900, 600, 'https://example.com/images/ipad11.jpg', '平板', 'Apple', '{"颜色":"银色","存储":"256GB","屏幕":"10.9英寸","芯片":"A16"}', 87650, 16540, FALSE, 1, NOW(), NOW(), 1, 1);

-- ======== 华为 MatePad (10条) ========
INSERT INTO item.item (name, price, stock, image, category, brand, spec, sold, comment_count, is_ad, status, create_time, update_time, creater, updater) VALUES
('华为 MatePad Pro 13.2 256GB 砚黑', 549900, 300, 'https://example.com/images/matepadpro132.jpg', '平板', '华为', '{"颜色":"砚黑","存储":"256GB","屏幕":"13.2英寸OLED","芯片":"麒麟9000s"}', 32450, 5980, TRUE, 1, NOW(), NOW(), 1, 1),
('华为 MatePad Pro 13.2 512GB 晶钻白', 649900, 180, 'https://example.com/images/matepadpro132.jpg', '平板', '华为', '{"颜色":"晶钻白","存储":"512GB","屏幕":"13.2英寸OLED","芯片":"麒麟9000s"}', 21340, 3980, FALSE, 1, NOW(), NOW(), 1, 1),
('华为 MatePad Pro 11 256GB 星河蓝', 429900, 350, 'https://example.com/images/matepadpro11.jpg', '平板', '华为', '{"颜色":"星河蓝","存储":"256GB","屏幕":"11英寸OLED","芯片":"麒麟9000s"}', 28760, 5230, TRUE, 1, NOW(), NOW(), 1, 1),
('华为 MatePad Pro 11 512GB 曜金黑', 509900, 220, 'https://example.com/images/matepadpro11.jpg', '平板', '华为', '{"颜色":"曜金黑","存储":"512GB","屏幕":"11英寸OLED","芯片":"麒麟9000s"}', 19870, 3670, FALSE, 1, NOW(), NOW(), 1, 1),
('华为 MatePad Air 12 256GB 羽砂白', 349900, 400, 'https://example.com/images/matepadair12.jpg', '平板', '华为', '{"颜色":"羽砂白","存储":"256GB","屏幕":"12英寸","芯片":"麒麟9000w"}', 38650, 7120, FALSE, 1, NOW(), NOW(), 1, 1),
('华为 MatePad Air 12 512GB 曜石灰', 429900, 280, 'https://example.com/images/matepadair12.jpg', '平板', '华为', '{"颜色":"曜石灰","存储":"512GB","屏幕":"12英寸","芯片":"麒麟9000w"}', 26780, 4980, FALSE, 1, NOW(), NOW(), 1, 1),
('华为 MatePad 11.5 SE 128GB 海岛蓝', 199900, 500, 'https://example.com/images/matepad115se.jpg', '平板', '华为', '{"颜色":"海岛蓝","存储":"128GB","屏幕":"11.5英寸","芯片":"麒麟710A"}', 56780, 10450, FALSE, 1, NOW(), NOW(), 1, 1),
('华为 MatePad 11.5 SE 256GB 星云灰', 249900, 400, 'https://example.com/images/matepad115se.jpg', '平板', '华为', '{"颜色":"星云灰","存储":"256GB","屏幕":"11.5英寸","芯片":"麒麟710A"}', 43210, 7890, FALSE, 1, NOW(), NOW(), 1, 1),
('华为 MatePad SE 11 128GB 星云灰', 149900, 600, 'https://example.com/images/matepadse11.jpg', '平板', '华为', '{"颜色":"星云灰","存储":"128GB","屏幕":"11英寸","芯片":"麒麟8系列"}', 87650, 16540, FALSE, 1, NOW(), NOW(), 1, 1),
('华为 MatePad Pro 13.2 柔光版 512GB 晶钻白', 699900, 120, 'https://example.com/images/matepadpro132-paper.jpg', '平板', '华为', '{"颜色":"晶钻白","存储":"512GB","屏幕":"13.2英寸柔光屏","芯片":"麒麟9000s"}', 12340, 2340, TRUE, 1, NOW(), NOW(), 1, 1);

-- ======== 小米平板 (5条) ========
INSERT INTO item.item (name, price, stock, image, category, brand, spec, sold, comment_count, is_ad, status, create_time, update_time, creater, updater) VALUES
('小米平板 7 Pro 256GB 黑色', 329900, 350, 'https://example.com/images/mipad7pro.jpg', '平板', '小米', '{"颜色":"黑色","存储":"256GB","屏幕":"12.4英寸","芯片":"骁龙8 Gen2"}', 38920, 7230, TRUE, 1, NOW(), NOW(), 1, 1),
('小米平板 7 Pro 512GB 远山蓝', 399900, 220, 'https://example.com/images/mipad7pro.jpg', '平板', '小米', '{"颜色":"远山蓝","存储":"512GB","屏幕":"12.4英寸","芯片":"骁龙8 Gen2"}', 27650, 5120, FALSE, 1, NOW(), NOW(), 1, 1),
('小米平板 7 256GB 深空灰', 249900, 450, 'https://example.com/images/mipad7.jpg', '平板', '小米', '{"颜色":"深空灰","存储":"256GB","屏幕":"11英寸","芯片":"骁龙7+ Gen3"}', 43210, 8120, FALSE, 1, NOW(), NOW(), 1, 1),
('小米平板 6S Pro 512GB 黑色', 299900, 300, 'https://example.com/images/mipad6spro.jpg', '平板', '小米', '{"颜色":"黑色","存储":"512GB","屏幕":"12.4英寸","芯片":"骁龙8 Gen2"}', 56780, 10450, FALSE, 1, NOW(), NOW(), 1, 1),
('小米平板 6 256GB 烟青绿', 179900, 500, 'https://example.com/images/mipad6.jpg', '平板', '小米', '{"颜色":"烟青绿","存储":"256GB","屏幕":"11英寸","芯片":"骁龙870"}', 98760, 18790, FALSE, 1, NOW(), NOW(), 1, 1);

-- ======== Samsung平板 (5条) ========
INSERT INTO item.item (name, price, stock, image, category, brand, spec, sold, comment_count, is_ad, status, create_time, update_time, creater, updater) VALUES
('Samsung Galaxy Tab S10 Ultra 256GB 钛灰色', 799900, 150, 'https://example.com/images/tabs10ultra.jpg', '平板', 'Samsung', '{"颜色":"钛灰色","存储":"256GB","屏幕":"14.6英寸","芯片":"天玑9300+"}', 19870, 3780, TRUE, 1, NOW(), NOW(), 1, 1),
('Samsung Galaxy Tab S10 Ultra 512GB 钛黑色', 949900, 100, 'https://example.com/images/tabs10ultra.jpg', '平板', 'Samsung', '{"颜色":"钛黑色","存储":"512GB","屏幕":"14.6英寸","芯片":"天玑9300+"}', 13450, 2670, FALSE, 1, NOW(), NOW(), 1, 1),
('Samsung Galaxy Tab S10+ 256GB 雅岩灰', 599900, 200, 'https://example.com/images/tabs10plus.jpg', '平板', 'Samsung', '{"颜色":"雅岩灰","存储":"256GB","屏幕":"12.4英寸","芯片":"天玑9300+"}', 28760, 5340, FALSE, 1, NOW(), NOW(), 1, 1),
('Samsung Galaxy Tab S9 FE 256GB 薄荷绿', 349900, 300, 'https://example.com/images/tabs9fe.jpg', '平板', 'Samsung', '{"颜色":"薄荷绿","存储":"256GB","屏幕":"10.9英寸","芯片":"Exynos 1380"}', 45670, 8760, FALSE, 1, NOW(), NOW(), 1, 1),
('Samsung Galaxy Tab S9 FE+ 256GB 薰衣紫', 429900, 220, 'https://example.com/images/tabs9feplus.jpg', '平板', 'Samsung', '{"颜色":"薰衣紫","存储":"256GB","屏幕":"12.4英寸","芯片":"Exynos 1380"}', 34560, 6540, FALSE, 1, NOW(), NOW(), 1, 1);

-- ======== Apple MacBook (5条) ========
INSERT INTO item.item (name, price, stock, image, category, brand, spec, sold, comment_count, is_ad, status, create_time, update_time, creater, updater) VALUES
('MacBook Pro 14 M4 512GB 深空黑色', 1499900, 200, 'https://example.com/images/mbp14m4.jpg', '电脑', 'Apple', '{"颜色":"深空黑色","存储":"512GB","屏幕":"14.2英寸","芯片":"M4","内存":"16GB"}', 21340, 4230, TRUE, 1, NOW(), NOW(), 1, 1),
('MacBook Pro 14 M4 Pro 1TB 银色', 1999900, 120, 'https://example.com/images/mbp14m4pro.jpg', '电脑', 'Apple', '{"颜色":"银色","存储":"1TB","屏幕":"14.2英寸","芯片":"M4 Pro","内存":"24GB"}', 15670, 3120, TRUE, 1, NOW(), NOW(), 1, 1),
('MacBook Pro 16 M4 Max 1TB 深空黑色', 2799900, 60, 'https://example.com/images/mbp16m4max.jpg', '电脑', 'Apple', '{"颜色":"深空黑色","存储":"1TB","屏幕":"16.2英寸","芯片":"M4 Max","内存":"36GB"}', 9870, 1980, FALSE, 1, NOW(), NOW(), 1, 1),
('MacBook Air 15 M3 512GB 午夜色', 1099900, 300, 'https://example.com/images/mba15m3.jpg', '电脑', 'Apple', '{"颜色":"午夜色","存储":"512GB","屏幕":"15.3英寸","芯片":"M3","内存":"16GB"}', 38920, 7230, FALSE, 1, NOW(), NOW(), 1, 1),
('MacBook Air 13 M3 256GB 星光色', 899900, 400, 'https://example.com/images/mba13m3.jpg', '电脑', 'Apple', '{"颜色":"星光色","存储":"256GB","屏幕":"13.6英寸","芯片":"M3","内存":"8GB"}', 54320, 10230, FALSE, 1, NOW(), NOW(), 1, 1);

-- ======== 华为 MateBook (5条) ========
INSERT INTO item.item (name, price, stock, image, category, brand, spec, sold, comment_count, is_ad, status, create_time, update_time, creater, updater) VALUES
('华为 MateBook X Pro 2025 1TB 砚黑', 1299900, 150, 'https://example.com/images/matebookxpro2025.jpg', '电脑', '华为', '{"颜色":"砚黑","存储":"1TB","屏幕":"14.2英寸OLED","芯片":"Intel Ultra 9","内存":"32GB"}', 12340, 2450, TRUE, 1, NOW(), NOW(), 1, 1),
('华为 MateBook X Pro 2025 512GB 皓月银', 1099900, 200, 'https://example.com/images/matebookxpro2025.jpg', '电脑', '华为', '{"颜色":"皓月银","存储":"512GB","屏幕":"14.2英寸OLED","芯片":"Intel Ultra 7","内存":"16GB"}', 9870, 1890, FALSE, 1, NOW(), NOW(), 1, 1),
('华为 MateBook 16s 512GB 深空灰', 799900, 250, 'https://example.com/images/matebook16s.jpg', '电脑', '华为', '{"颜色":"深空灰","存储":"512GB","屏幕":"16英寸","芯片":"Intel Ultra 7","内存":"16GB"}', 16780, 3120, FALSE, 1, NOW(), NOW(), 1, 1),
('华为 MateBook D 16 512GB 深空灰', 549900, 400, 'https://example.com/images/matebookd16.jpg', '电脑', '华为', '{"颜色":"深空灰","存储":"512GB","屏幕":"16英寸","芯片":"Intel Ultra 5","内存":"16GB"}', 28760, 5340, FALSE, 1, NOW(), NOW(), 1, 1),
('华为 MateBook 14 512GB 皓月银', 629900, 350, 'https://example.com/images/matebook14.jpg', '电脑', '华为', '{"颜色":"皓月银","存储":"512GB","屏幕":"14英寸","芯片":"Intel Ultra 5","内存":"16GB"}', 34560, 6450, FALSE, 1, NOW(), NOW(), 1, 1);

-- ======== 联想 (5条) ========
INSERT INTO item.item (name, price, stock, image, category, brand, spec, sold, comment_count, is_ad, status, create_time, update_time, creater, updater) VALUES
('ThinkPad X1 Carbon Gen 13 512GB 黑色', 1199900, 150, 'https://example.com/images/x1cgen13.jpg', '电脑', '联想', '{"颜色":"黑色","存储":"512GB","屏幕":"14英寸","芯片":"Intel Ultra 7","内存":"32GB"}', 9870, 1980, TRUE, 1, NOW(), NOW(), 1, 1),
('ThinkPad X1 Carbon Gen 13 1TB 黑色', 1499900, 80, 'https://example.com/images/x1cgen13.jpg', '电脑', '联想', '{"颜色":"黑色","存储":"1TB","屏幕":"14英寸","芯片":"Intel Ultra 7","内存":"32GB"}', 6540, 1340, FALSE, 1, NOW(), NOW(), 1, 1),
('联想 Yoga Pro 14s 512GB 深空灰', 799900, 200, 'https://example.com/images/yogapro14s.jpg', '电脑', '联想', '{"颜色":"深空灰","存储":"512GB","屏幕":"14.5英寸","芯片":"Intel Ultra 7","内存":"32GB"}', 16780, 3120, FALSE, 1, NOW(), NOW(), 1, 1),
('联想 小新 Pro 16 512GB 鸽子灰', 649900, 350, 'https://example.com/images/xiaoxinpro16.jpg', '电脑', '联想', '{"颜色":"鸽子灰","存储":"512GB","屏幕":"16英寸","芯片":"Intel Ultra 5","内存":"16GB"}', 34560, 6540, FALSE, 1, NOW(), NOW(), 1, 1),
('联想 小新 14 512GB 霜雪银', 499900, 500, 'https://example.com/images/xiaoxin14.jpg', '电脑', '联想', '{"颜色":"霜雪银","存储":"512GB","屏幕":"14英寸","芯片":"Intel Ultra 5","内存":"16GB"}', 56780, 10450, FALSE, 1, NOW(), NOW(), 1, 1);

-- ======== Dell (5条) ========
INSERT INTO item.item (name, price, stock, image, category, brand, spec, sold, comment_count, is_ad, status, create_time, update_time, creater, updater) VALUES
('Dell XPS 14 512GB 铂金银', 1199900, 120, 'https://example.com/images/xps14.jpg', '电脑', 'Dell', '{"颜色":"铂金银","存储":"512GB","屏幕":"14.5英寸OLED","芯片":"Intel Ultra 7","内存":"16GB"}', 8790, 1760, TRUE, 1, NOW(), NOW(), 1, 1),
('Dell XPS 14 1TB 石墨黑', 1499900, 80, 'https://example.com/images/xps14.jpg', '电脑', 'Dell', '{"颜色":"石墨黑","存储":"1TB","屏幕":"14.5英寸OLED","芯片":"Intel Ultra 9","内存":"32GB"}', 5670, 1120, FALSE, 1, NOW(), NOW(), 1, 1),
('Dell XPS 16 1TB 铂金银', 1799900, 50, 'https://example.com/images/xps16.jpg', '电脑', 'Dell', '{"颜色":"铂金银","存储":"1TB","屏幕":"16.3英寸OLED","芯片":"Intel Ultra 9","内存":"32GB"}', 4320, 890, FALSE, 1, NOW(), NOW(), 1, 1),
('Dell Inspiron 16 Plus 512GB 冰河蓝', 699900, 250, 'https://example.com/images/inspiron16plus.jpg', '电脑', 'Dell', '{"颜色":"冰河蓝","存储":"512GB","屏幕":"16英寸","芯片":"Intel Ultra 7","内存":"16GB"}', 21340, 4230, FALSE, 1, NOW(), NOW(), 1, 1),
('Dell Inspiron 14 512GB 铂金银', 549900, 350, 'https://example.com/images/inspiron14.jpg', '电脑', 'Dell', '{"颜色":"铂金银","存储":"512GB","屏幕":"14英寸","芯片":"Intel Ultra 5","内存":"16GB"}', 34560, 6540, FALSE, 1, NOW(), NOW(), 1, 1);
