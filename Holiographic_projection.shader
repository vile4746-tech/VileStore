Shader "Unlit/Holiographic_projection"
{
    Properties
    {
        _MainTex ("Texture", 2D) = "white" {}
        _SMcolor ("SaoMiaoColor",Color) = (0,0,0,1)
        _BYcolor ("BianYvanGuangColor",Color) = (0,0,0,1)

        _alpha ("Toumingdu",range(0,1)) = 1
        _alphaSM ("SMalpha",range(0,1)) = 1
        _alphaBY ("BYalpha",range(0,1)) = 1
        _alphaMAIN ("MAINalpha",range(0,1)) = 1
    }
    SubShader
    {
        Tags { "RenderType"="OTransparentpaque" "Queue" = "Transparent"}
        LOD 100

        Pass
        {
            ZWrite off
            Blend SrcAlpha OneMinusSrcAlpha

            HLSLPROGRAM
            #pragma vertex vert
            #pragma fragment frag
            // make fog work
            #pragma multi_compile_fog

            #include "Packages/com.unity.render-pipelines.universal/ShaderLibrary/Core.hlsl"
            #include "Packages/com.unity.render-pipelines.core/Runtime/Utilities/Blit.hlsl"

            struct appdata
            {
                float4 vertex : POSITION;
                float3 normal : NORMAL;
                float2 uv : TEXCOORD0;
            };

            struct v2f
            {
                float4 vertex : SV_POSITION;

                float3 vertexWORLDS : TEXCOORD2;

                float3 cameraV : TEXCOORD1;
                float3 normal : NORMAL;

                float2 uv : TEXCOORD0;

                
            };

            v2f vert (appdata v)
            {
                v2f o;
                //o.vertex = TransformObjectToHClip(v.vertex.xyz);
                o.vertex = TransformWorldToHClip(o.vertexWORLDS = TransformObjectToWorld(v.vertex.xyz));
                o.uv = v.uv;

                o.cameraV = GetCameraPositionWS() - o.vertexWORLDS;

                //o.cameraV = _WorldSpaceCameraPos - (mul(unity_ObjectToWorld, v.vertex).xyz);
                o.normal = TransformObjectToWorldNormal(v.normal);

                return o;
            }

            sampler2D _MainTex;
            half4 _SMcolor;
            half4 _BYcolor;

            half _alpha,_alphaMAIN,_alphaBY,_alphaSM;

            //frac(i.vertex.x / _ScreenParams.x)
            half4 frag (v2f i) : SV_Target
            {
                float lineStrength = frac(i.vertexWORLDS.y - _Time.y);
                lineStrength = smoothstep(0.6,1,lineStrength);
                lineStrength *= 10;

                float bianyvanStrength =  1 - dot(normalize(i.normal),normalize(i.cameraV));
                bianyvanStrength *= bianyvanStrength;
                bianyvanStrength *= 0.35;
               
                
                half4 ret = (half4(0,0.05,0.1,1)* _alphaMAIN) + (bianyvanStrength * _BYcolor * _alphaBY) + (lineStrength * _SMcolor * _alphaSM);
                ret.w = _alpha;

                return ret;
            }
            ENDHLSL
        }
    }
}
