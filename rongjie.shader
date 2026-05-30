Shader "Unlit/rongjie"
{
    Properties
    {
        _MainTexure ("mainTexture", 2D) = "white" {}
        _value ("value",range(0,1)) = 0.5
        _speed ("Speed",range(0,10)) = 0.2
    }

    


    SubShader
    {
        Tags { "RenderType"="Opaque" }
        LOD 100

        Pass
        {
            CGPROGRAM
            #pragma vertex vert
            #pragma fragment frag
            // make fog work
            #pragma multi_compile_fog

            #include "UnityCG.cginc"

            struct appdata
            {
                float4 vertex : POSITION;
                float2 uv : TEXCOORD0;
            };

            struct v2f
            {
                float2 uv : TEXCOORD0;
                UNITY_FOG_COORDS(1)
                float4 vertex : SV_POSITION;
            };

            sampler2D _MainTexure;
            float _value;
            float _speed;

            v2f vert (appdata v)
            {
                v2f o;
                o.vertex = UnityObjectToClipPos(v.vertex);
                o.uv = v.uv;
                UNITY_TRANSFER_FOG(o,o.vertex);
                return o;
            }

            fixed4 frag (v2f i) : SV_Target
            {
                // sample the texture
                float noise = tex2D(_MainTexure,  i.uv + _speed * _Time.y ).r;
                clip(noise - _value);


                
                return fixed4(1,1,1,0);
            }
            ENDCG
        }
    }
}
