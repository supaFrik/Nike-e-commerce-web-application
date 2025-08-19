# Script để cập nhật tất cả sản phẩm trong boloc.html
$filePath = "boloc.html"

# Đọc nội dung file
$content = Get-Content $filePath -Raw

# Thay đổi class từ "product" thành "product-card"
$content = $content -replace 'class="product"', 'class="product-card"'

# Xóa thẻ <a> trong product-title và chỉ giữ lại text
$content = $content -replace '<div class="product-title"><a href="[^"]*">([^<]*)</a></div>', '<div class="product-title">$1</div>'

# Thêm onclick cho tất cả product-card chưa có
$productCards = @()

# Danh sách các ID sản phẩm
$productIds = @(
    'ao-polo-nam-the-thao-promax-s1',
    'ao-polo-nam-pique-cotton', 
    'ao-tshirt-trang-nam-premium-aircool',
    'ao-polo-nam-linen',
    'ao-polo-nam-the-thao-promax-s1-2',
    'ao-polo-nam-pique-cotton-mem',
    'ao-polo-nam-premium-aircool',
    'ao-polo-nam-linen-2',
    'ao-polo-nam-the-thao-promax-s1-3',
    'ao-polo-nam-pique-cotton-3',
    'ao-polo-nam-premium-aircool-2',
    'ao-polo-nu-the-thao-promax-s1',
    'ao-polo-nu-pique-cotton',
    'ao-polo-nu-premium-aircool',
    'ao-polo-nu-linen'
)

# Thêm onclick cho các sản phẩm chưa có
$pattern = '<div class="product-card"([^>]*?)>'
$replacement = '<div class="product-card" onclick="window.location.href=''product-detail-dynamic.html?id={0}''"$1>'

$index = 0
$content = [regex]::Replace($content, $pattern, {
    param($match)
    $id = if ($index -lt $productIds.Count) { $productIds[$index] } else { "product-$index" }
    $script:index++
    return ('<div class="product-card" onclick="window.location.href=''product-detail-dynamic.html?id=' + $id + '''"' + $match.Groups[1].Value + '>')
})

# Ghi lại file
Set-Content $filePath $content

Write-Host "Đã cập nhật tất cả sản phẩm thành công!"
