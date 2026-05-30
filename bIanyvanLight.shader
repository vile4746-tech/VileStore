Shader "Unlit/bIanyvanLight"
{
    Properties
    {
        _MainTex ("Texture", 2D) = "white" {}
    }
    SubShader
    {

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
                float3 normal : NORMAL;
                float2 uv : TEXCOORD0;
            };

            struct v2f
            {
                float4 pos : SV_POSITION;
                float2 uv : TEXCOORD0;

                float3 normal : TEXCOORD1;
                float3 cameraV : TEXCOORD2;
            };

            sampler2D _MainTex;

            v2f vert (appdata v)
            {
                v2f o;
                o.pos = UnityObjectToClipPos(v.vertex);
                o.uv = v.uv;
                o.normal = UnityObjectToWorldNormal(v.normal);
                o.cameraV = _WorldSpaceCameraPos - (mul(unity_ObjectToWorld, v.vertex).xyz);
                return o;
            }

            fixed4 frag (v2f i) : SV_Target
            {
                float f = 1- dot(normalize(i.normal),normalize(i.cameraV));
                f *= f;

                float v = 0.35 * f;
                return fixed4(1,0,0,0) * (1 - f) + (fixed4(1,1,1,1) * v);
            }
            ENDCG
        }
    }
}
