# DotLoader

Progress loader with customizable dot size, amount, delay in animation and pulse strength.

### Sample

<p float="left">
<img src="https://user-images.githubusercontent.com/71257281/125494534-faee40dc-6509-47e6-8386-34527b0b2af6.gif" width="200">
<img src="https://user-images.githubusercontent.com/71257281/125497195-789878c2-3510-4ec8-9ca3-438311979e1e.gif" width="200">
<img src="https://user-images.githubusercontent.com/71257281/125496635-4655453a-74e7-4ec4-8791-4551bd05c4db.gif" width="200">
</p>

### Usage

```xml
<com.artkachenko.dotloader.DotLoader
        android:id="@+id/loader"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:visibility="gone"
        app:delay="100"
        app:item_distance="8"
        app:dot_size="24"
        app:dot_amount="4"
        app:pulse_strength="0.6"/>
```

delay - delay betweeen item animations  
item_distance - distance between items in dp   
dot_size - size of the items in dp  
dot_amount - amount of dots  
pulse_strength - scaling for pulse to be done on each item. 0.9 will scale it 10%, 0.3 will scale it 70% etc  

